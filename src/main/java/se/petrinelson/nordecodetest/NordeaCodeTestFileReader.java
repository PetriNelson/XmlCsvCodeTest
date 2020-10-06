package se.petrinelson.nordecodetest;

import se.petrinelson.nordecodetest.printer.CSVPrinter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

public class NordeaCodeTestFileReader {
    static private LineProcessor lineProcessor;
    static private CSVPrinter csvPrinter;

    public static void main(String[] args) throws Exception {
        csvPrinter = new CSVPrinter();
        lineProcessor = new LineProcessor(csvPrinter);
        BufferedReader br = null;
        try {
            br  = new BufferedReader(new FileReader("C:\\NordeaCodeTest\\src\\main\\resources\\sampledata\\large.in"));
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                count++;
                //Find a sentence
                String regex = " *[.!?]";
                String[] split = line.split(regex);
                lineProcessor.setLine(line);
                lineProcessor.setSentences(Arrays.<String>asList(split));
                lineProcessor.processLine();
            }
            csvPrinter.finalflush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            br.close();
        }

    }
}