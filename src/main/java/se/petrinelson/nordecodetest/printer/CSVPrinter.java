package se.petrinelson.nordecodetest.printer;

import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.petrinelson.nordecodetest.domain.Sentence;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class CSVPrinter implements OutputPrinter {
    private static final Logger LOGGER = LogManager.getLogger(CSVPrinter.class);

    private List<Sentence> sentenceList = new ArrayList<Sentence>();
    private Integer totalNrOfSentences= 0;
    private Integer totalNrOfWords = 0;
    private Integer currentPrintedSentenceIndex = 1;
    private OptionalInt maxNrOfWords = OptionalInt.empty();
    private final int FLUSH_LIMIT = 100;
    private boolean printHasStarted;
    private CSVWriter csvWriter = null;
    private final String CSV_OUTPUTFILE_RECORDS = "CSV_Output_records.csv";
    private final String CSV_OUTPUTFILE_HEADER = "CSV_Output_header.csv";
    private final String CSV_OUTPUTFILE = "CSV_Output.csv";
    private FileWriter outputfile = null;
    private boolean isflushDone = false;

    @Override
    public void addSentenceAndFlush(Sentence sentence) {
        sentenceList.add(sentence);
        collectStatistics(sentence);
        try {
            flushOutput(false);
        } catch (IOException e) {
            LOGGER.error("Error log message", e);
        }
    }

    private void collectStatistics(Sentence sentence){
        totalNrOfSentences++;
        totalNrOfWords = totalNrOfWords + sentence.getWords().size();
        OptionalInt tmpMap = sentenceList.stream().mapToInt(value -> value.getWords().size()).max();
        if(!maxNrOfWords.isPresent()){
            maxNrOfWords = tmpMap;
        } else if(maxNrOfWords.isPresent() && tmpMap.isPresent() && tmpMap.getAsInt()  > maxNrOfWords.getAsInt() ){
            maxNrOfWords =  tmpMap;
        }
    }

    @Override
    public void finalflush() throws Exception {
        LOGGER.debug("CSV is about to finish the export!");
        flushOutput(true);
    }

    public void flushOutput(boolean isFinished) throws IOException {
        List<String[]> rows = new ArrayList<>();

        if(sentenceList.size() < FLUSH_LIMIT){
            if(!isFinished){
                return;
            }
        }

        if(!printHasStarted){
            printHasStarted = true;
            if(Files.exists(Paths.get(CSV_OUTPUTFILE))){
                Files.delete(Paths.get(CSV_OUTPUTFILE));
            }
            outputfile = new FileWriter(CSV_OUTPUTFILE_RECORDS);
            csvWriter = new CSVWriter(outputfile,
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
        }

        addRowRecords(csvWriter, rows);
        csvWriter.writeAll(rows);
        addRowHeader(csvWriter, isFinished);
    }

    @Override
    public List<Sentence> getListOfSentences() {
        return sentenceList;
    }

    private void addRowRecords(CSVWriter csvWriter, List<String[]> rows ) {
        ListIterator<Sentence> sentenceListIterator = sentenceList.listIterator();

        while (sentenceListIterator.hasNext()) {
            Sentence sentenceTmp = sentenceListIterator.next();
            List<String> tmpRec = new ArrayList<>();
            StringBuffer recBuf = new StringBuffer();
            recBuf.append("Sentence ").append(currentPrintedSentenceIndex);
            tmpRec.add(recBuf.toString());
            sentenceTmp.getWords().stream().forEach(word -> tmpRec.add(word.getValue()));
            rows.add(tmpRec.stream().toArray(String[]::new));
            //Remove it after it has been printed out
            sentenceListIterator.remove();
            currentPrintedSentenceIndex++;
        }
    }

    private void addRowHeader(CSVWriter csvWriter, boolean isFinished) throws IOException {
        //Special handling this header is added to the first row at the end of the export
        if(!isFinished){
            return;
        }

        //First close the CSV writer to the file
        csvWriter.flush();
        csvWriter.close();

        //Add the header to top of existing file
        FileWriter cvsOutputHeader = new FileWriter(CSV_OUTPUTFILE_HEADER);

        int nrOfWords = maxNrOfWords.getAsInt();
        List<String> headerRow = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < nrOfWords ; i++) {
            count++;
            if(count == 1){
                headerRow.add(" ");
            }
            headerRow.add(new String("Word ") + count);
        }
        String header = headerRow.stream()
                .map( Object::toString )
                .collect( Collectors.joining( "," ) );
        BufferedWriter bw = new BufferedWriter(cvsOutputHeader);
        bw.write(header + System.lineSeparator());
        bw.close();

        concatHeaderAndRecords();
        cleanupFiles();

    }

    private void cleanupFiles() throws IOException {
        if (Files.exists(Paths.get(CSV_OUTPUTFILE_RECORDS))) {
            Files.delete(Paths.get(CSV_OUTPUTFILE_RECORDS));
        }
        if (Files.exists(Paths.get(CSV_OUTPUTFILE_HEADER))) {
            Files.delete(Paths.get(CSV_OUTPUTFILE_HEADER));
        }
    }

    private void concatHeaderAndRecords() throws IOException {
        //Concat the two files
        InputStream headerRecord = new FileInputStream(CSV_OUTPUTFILE_HEADER);
        InputStream recordRows = new FileInputStream(CSV_OUTPUTFILE_RECORDS);

        SequenceInputStream concatInputStream =
                new SequenceInputStream(headerRecord, recordRows);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(CSV_OUTPUTFILE));

        byte[] bys = new byte[1024];
        int len = 0;
        while ((len = concatInputStream.read(bys)) != -1) {
            bos.write(bys, 0, len);
        }
        bos.close();
        bos.close();
    }


    public Integer getTotalNrOfSentences() {
        return totalNrOfSentences;
    }

    public Integer getTotalNrOfWords() {
        return totalNrOfWords;
    }
}
