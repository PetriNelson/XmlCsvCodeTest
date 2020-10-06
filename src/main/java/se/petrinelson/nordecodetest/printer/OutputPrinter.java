package se.petrinelson.nordecodetest.printer;

import se.petrinelson.nordecodetest.domain.Sentence;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;

public interface OutputPrinter {
    void addSentenceAndFlush(final Sentence sentence);
    List<Sentence> getListOfSentences();
    void finalflush() throws Exception;
}
