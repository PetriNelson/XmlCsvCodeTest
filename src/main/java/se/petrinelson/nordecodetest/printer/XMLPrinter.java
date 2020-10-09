package se.petrinelson.nordecodetest.printer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.petrinelson.nordecodetest.domain.Sentence;
import se.petrinelson.nordecodetest.domain.Word;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class XMLPrinter implements OutputPrinter {
    private static final Logger LOGGER = LogManager.getLogger(XMLPrinter.class);

    private List<Sentence> sentenceList = new ArrayList<Sentence>();

    private Integer totalNrOfSentences= 0;
    private Integer totalNrOfWords = 0;
    private static  int FLUSH_LIMIT = 10000;
    private boolean printHasStarted;
    private XMLOutputFactory factory = XMLOutputFactory.newInstance();
    private ByteArrayOutputStream out = null;
    private XMLStreamWriter writer = null;

    private final String ENCODING = "UTF-8";
    private final String TEXT_ELEMENT = "text";
    private final String SENTENCE_ELEMENT = "sentence";
    private final String WORD_ELEMENT = "word";
    private final String VERSION_VALUE = "1.0";
    private final String ENCODING_VALUE = "UTF-8";

    @Override
    public void addSentenceAndFlush(Sentence sentence) {
        sentenceList.add(sentence);
        collectStatistics(sentence);
        try {
            flushOutput(false);
        } catch (XMLStreamException e) {
            LOGGER.error("Error log message", e);
        }
    }

    private void collectStatistics(Sentence sentence){
        totalNrOfSentences++;
        totalNrOfWords = totalNrOfWords + sentence.getWords().size();
    }

    @Override
    public void finalflush() throws XMLStreamException {
        flushOutput(true);
    }

    @Override
    public List<Sentence> getListOfSentences() {
        return sentenceList;
    }

    private void flushOutput(boolean isFinished) throws XMLStreamException {
        if(sentenceList.size() < FLUSH_LIMIT){
            if(!isFinished){
                return;
            }
        }
        //Activate only once when start output stream
        if(!printHasStarted){
            printHasStarted = true;
            out = new ByteArrayOutputStream();
            writer = factory.createXMLStreamWriter(out, ENCODING );
            printHeaderElement(writer);
        }

        ListIterator<Sentence> sentenceIter = sentenceList.listIterator();
        while (sentenceIter.hasNext()) {
            Sentence tmpSentence = sentenceIter.next();
            printSentenceElement(tmpSentence, writer);
            //Remove it after it has been printed out
            sentenceIter.remove();
        }
        //This is only called whe user has entered CTRL+D
        printFooterElement(isFinished, writer);
        System.out.println(out.toString());
    }

    private void printHeaderElement(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeCharacters("\n");
        writer.writeCharacters("\n");
        writer.writeCharacters("================================    START XML RESULT    ========================================");
        writer.writeCharacters("\n");
        writer.writeStartDocument(ENCODING_VALUE, VERSION_VALUE);
        writer.writeCharacters("\n");
        writer.writeStartElement(TEXT_ELEMENT);
        writer.writeCharacters("\n");
    }

    /*
     This should be called when user have stopped the input stream with CTRL +D
     */
    private void printFooterElement(boolean isFinished, XMLStreamWriter writer) throws XMLStreamException {
        if(!isFinished){
            writer.flush();
            return;
        }
        writer.writeEndElement();//End element <text>
        writer.writeCharacters("\n");
        writer.writeCharacters("======  END XML RESULT  Total nr of Sentences=" + getTotalNrOfSentences()+ ", with total Words=" + getTotalNrOfWords()+ "  =========");
        writer.flush();
        writer.close();
    }

    private void printSentenceElement(Sentence sentence, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeCharacters("\t");
        writer.writeStartElement(SENTENCE_ELEMENT);
        writer.writeCharacters("\n");
        ListIterator<Word> wordListIterator = sentence.getWords().listIterator();
        while (wordListIterator.hasNext()) {
            Word wordTmp = wordListIterator.next();
            writer.writeCharacters("\t\t");
            writer.writeStartElement(WORD_ELEMENT);
            writer.writeCharacters(wordTmp.getValue());
            writer.writeEndElement();
            writer.writeCharacters("\n");
        }
        writer.writeCharacters("\t");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    public Integer getTotalNrOfSentences() {
        return totalNrOfSentences;
    }

    public Integer getTotalNrOfWords() {
        return totalNrOfWords;
    }
}
