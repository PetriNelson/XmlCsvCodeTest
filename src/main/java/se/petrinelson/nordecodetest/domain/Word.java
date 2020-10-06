package se.petrinelson.nordecodetest.domain;

import java.util.Comparator;

public class Word implements Comparable<Word> {
    private String value;
    private Comparator comparator;

    @Override
    public int compareTo(Word otherObject) {
        return this.getValue().toLowerCase().compareToIgnoreCase(otherObject.getValue().toLowerCase());
    }

    public Word(String word) {
        this.value = word;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
