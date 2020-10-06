package se.petrinelson.nordecodetest;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import se.petrinelson.nordecodetest.domain.Sentence;
import se.petrinelson.nordecodetest.printer.XMLPrinter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class XMLPrinterTest extends TestCase {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void testAddSentenceAndFlush() {
        XMLPrinter xmlPrinter = new XMLPrinter();
        ReflectionTestUtils.setField(xmlPrinter, "FLUSH_LIMIT", 1);
        Sentence testSentence = new Sentence("This is not a sentence, because it has missing dot as");
        xmlPrinter.addSentenceAndFlush(testSentence);
        Assert.assertEquals("================================    START XML RESULT    ========================================\n" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<text>\n" +
                "\t<sentence>\n" +
                "\t\t<word>a</word>\n" +
                "\t\t<word>as</word>\n" +
                "\t\t<word>because</word>\n" +
                "\t\t<word>dot</word>\n" +
                "\t\t<word>has</word>\n" +
                "\t\t<word>is</word>\n" +
                "\t\t<word>it</word>\n" +
                "\t\t<word>missing</word>\n" +
                "\t\t<word>not</word>\n" +
                "\t\t<word>sentence</word>\n" +
                "\t\t<word>This</word>\n" +
                "\t</sentence>", outputStreamCaptor.toString()
                .trim());
    }

    @Test
    public void testMultipleSentences() {
        XMLPrinter xmlPrinter = new XMLPrinter();
        ReflectionTestUtils.setField(xmlPrinter, "FLUSH_LIMIT", 1);
        Sentence testSentence = new Sentence("K A HD F S A S N CM S W E");
        xmlPrinter.addSentenceAndFlush(testSentence);
        Assert.assertEquals("================================    START XML RESULT    ========================================\n" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<text>\n" +
                "\t<sentence>\n" +
                "\t\t<word>A</word>\n" +
                "\t\t<word>A</word>\n" +
                "\t\t<word>CM</word>\n" +
                "\t\t<word>E</word>\n" +
                "\t\t<word>F</word>\n" +
                "\t\t<word>HD</word>\n" +
                "\t\t<word>K</word>\n" +
                "\t\t<word>N</word>\n" +
                "\t\t<word>S</word>\n" +
                "\t\t<word>S</word>\n" +
                "\t\t<word>S</word>\n" +
                "\t\t<word>W</word>\n" +
                "\t</sentence>", outputStreamCaptor.toString()
                .trim());
    }
}