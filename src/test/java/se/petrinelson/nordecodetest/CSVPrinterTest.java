package se.petrinelson.nordecodetest;

import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import se.petrinelson.nordecodetest.domain.Sentence;
import se.petrinelson.nordecodetest.printer.CSVPrinter;

public class CSVPrinterTest extends TestCase {

    @Test
    public void testAddSentenceAndFlush() {
        CSVPrinter csvPrinter = new CSVPrinter();
        ReflectionTestUtils.setField(csvPrinter, "FLUSH_LIMIT", 0);
        Sentence testSentence = new Sentence("This is not a sentence, because it has missing dot as");
        csvPrinter.addSentenceAndFlush(testSentence);
    }
}