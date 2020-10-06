package se.petrinelson.nordecodetest.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class Sentence {
    final static String WORD_REGEXP = "\\s+";
    List<Word> words;

    public Sentence(final String sentence) {
        //Do the outputhandling
        String[] split = sentence.split(WORD_REGEXP);
        initWordCollection(Arrays.<String>asList(split));
    }

    private void initWordCollection(final List<String> wordsList) {
        this.words = wordsList.stream().
                filter(s -> StringUtils.isNotEmpty(s)).
                map(Word::new).collect(Collectors.toList());
        //Preprocess string just to remove charachters
        ListIterator<Word> iter = words.listIterator();
        while (iter.hasNext()) {
            String tmp = iter.next().getValue();
            if( "-".equals(tmp) || ".".equals(tmp) || ".".equals(tmp)) {
                iter.remove();
            } else if(StringUtils.contains(tmp,",")) {
                //Check if the string is like word1,word2 then we have to split it to two separate words
                splitWordToMultiples(iter, tmp, ",");
            } else if(StringUtils.contains(tmp,".")) {
                splitWordToMultiples(iter, tmp, "[.]");
            } else if(StringUtils.contains(tmp,":")) {
                splitWordToMultiples(iter, tmp, "[:]");
            }
        }
        Collections.sort(words);
    }

    private void splitWordToMultiples(ListIterator<Word> iter, String tmp, String splitChar) {
        String[] splittedword = tmp.split(splitChar);
        if(splittedword.length > 1 ){
            iter.remove();
            for (String splitvalue: splittedword) {
                iter.add(new Word(splitvalue));
            }
        } else if(splittedword.length > 0 ) {
            iter.remove();
            iter.add(new Word(splittedword[0].replaceAll(splitChar, " ").trim()));
        } else {
            iter.remove();
        }
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }
}
