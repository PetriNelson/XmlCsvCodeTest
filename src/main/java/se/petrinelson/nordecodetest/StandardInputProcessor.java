package se.petrinelson.nordecodetest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.petrinelson.nordecodetest.printer.CSVPrinter;
import se.petrinelson.nordecodetest.printer.OutputPrinter;
import se.petrinelson.nordecodetest.printer.XMLPrinter;

import java.util.Arrays;
import java.util.Scanner;

public class StandardInputProcessor {
    private static final Logger LOGGER = LogManager.getLogger(StandardInputProcessor.class);

    private String outputFormat;
    private LineProcessor lineProcessor;
    private XMLPrinter xmlPrinter;
    private CSVPrinter csvPrinter;

    public StandardInputProcessor(XMLPrinter xmlPrinter, CSVPrinter csvPrinter) {
        this.lineProcessor = lineProcessor;
        this.xmlPrinter = xmlPrinter;
        this.csvPrinter = csvPrinter;
    }

    public void startInputProcessing() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter XML or CSV to define output:");
        setOutputFormat(scanner.nextLine());
        if(StringUtils.equalsIgnoreCase("XML", getOutputFormat()) || StringUtils.equalsIgnoreCase("CSV", getOutputFormat())){

        } else {
            System.out.println("Wrong output format, enter XML or CSV (generates a file CSV_Output.csv in this folder)");
            setOutputFormat(scanner.nextLine());
        }
        System.out.println("Enter some text or paste text, abort with CTRL +D or.");

        //Reading each line of file using Scanner class
        int lineNumber = 1;
        boolean isProperSentence = false;
        StringBuffer lastSentence = new StringBuffer();
        OutputPrinter outputPrinter = setOutPutPrinter(getOutputFormat());
        LineProcessor lineProcessor = new LineProcessor(outputPrinter);
        while(scanner.hasNextLine()){
            LOGGER.debug("Start processing line nr" + lineNumber);
            String line = scanner.nextLine();
            //Find a sentence
            String regex = " *[.!?]";
            String[] split = line.split(regex);
            lineProcessor.setLine(line);
            lineProcessor.setSentences(Arrays.<String>asList(split));
            lineProcessor.processLine();
            lineNumber++;
        }
        LOGGER.debug("End processing");
        //Force output to be written before it is closed
        outputPrinter.finalflush();
    }

    private OutputPrinter setOutPutPrinter(String outputFormat) {
        OutputPrinter outputPrinter = null;
        switch (outputFormat) {
            case "XML":
                LOGGER.debug("Activating XML printer");
                outputPrinter = new XMLPrinter();
                break;
            case "CSV":
                LOGGER.debug("Activating CSV printer");
                outputPrinter = new CSVPrinter();
                break;
            default:
                LOGGER.error("No valid printer is selected");
        }
        return outputPrinter;
    }


    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }
}
