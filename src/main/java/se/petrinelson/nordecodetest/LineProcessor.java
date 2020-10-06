package se.petrinelson.nordecodetest;


import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.petrinelson.nordecodetest.domain.Sentence;
import se.petrinelson.nordecodetest.printer.OutputPrinter;

import java.util.List;

public class LineProcessor {
    private static final Logger LOGGER = LogManager.getLogger(LineProcessor.class);

    private List<String> sentences;
    private String line;
    private StringBuffer partofSentence = new StringBuffer();
    private OutputPrinter outputPrinter;

    public LineProcessor(OutputPrinter printer) {
        this.outputPrinter = printer;
    }

    public void processLine(){
        LOGGER.debug("Start processLine");
        int nr = 0;
        Sentence sentenceRecord = null;
        for (String sentence :  sentences) {
            nr++;
            if(StringUtils.isEmpty(sentence)){
                break;
            }
            String appendedSentence = sentence;
            if(partofSentence.length() > 0){
                appendedSentence = partofSentence.append(" ").append(sentence).toString();
            }
            //Check only is last element is a properly ended sentence
            if(sentences.size() == nr ){
                if(isProperlyEndedSentence(line)){
                    resetStringBuffer();
                    if(appendedSentence.length() > 0){
                        sentenceRecord = new Sentence(appendedSentence);
                    } else {
                        sentenceRecord = new Sentence(sentence);
                    }
                } else {
                    if(appendedSentence.length() > 0){
                        partofSentence = new StringBuffer(appendedSentence);
                    } else {
                        partofSentence = new StringBuffer(sentence);
                    }
                    sentenceRecord = new Sentence(sentence);
                }
            } else if(nr == 1 && partofSentence.length() > 0) {
                resetStringBuffer();
                sentenceRecord = new Sentence(appendedSentence);
            } else {
                resetStringBuffer();
                sentenceRecord = new Sentence(sentence);
            }
            //Register the sentence to the XML/CSV writer
            if(partofSentence.length() == 0) {
                outputPrinter.addSentenceAndFlush(sentenceRecord);
            }
        }
        outputPrinter.getListOfSentences();
    }

    private void resetStringBuffer(){
        partofSentence.setLength(0);
    }

    private boolean isProperlyEndedSentence(String line){
        if(".".equalsIgnoreCase(line.substring(line.length() - 1)) ||
                "!".equalsIgnoreCase(line.substring(line.length() - 1)) ||
                "?".equalsIgnoreCase(line.substring(line.length() - 1)) ){
            return true;
        }
        return false;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public void setSentences(List<String> sentences) {
        this.sentences = sentences;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public StringBuffer getPartofSentence() {
        return partofSentence;
    }

    public void setPartofSentence(StringBuffer partofSentence) {
        this.partofSentence = partofSentence;
    }

    public OutputPrinter getOutputPrinter() {
        return outputPrinter;
    }

    public void setOutputPrinter(OutputPrinter outputPrinter) {
        this.outputPrinter = outputPrinter;
    }
}
