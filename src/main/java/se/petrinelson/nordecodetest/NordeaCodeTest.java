package se.petrinelson.nordecodetest;

import se.petrinelson.nordecodetest.printer.CSVPrinter;
import se.petrinelson.nordecodetest.printer.XMLPrinter;

public class NordeaCodeTest {
    private static  StandardInputProcessor standardInputProcessor;

    public static void main(String[] args) throws Exception {
        standardInputProcessor = new StandardInputProcessor(new XMLPrinter(), new CSVPrinter());
        standardInputProcessor.startInputProcessing();
    }

}