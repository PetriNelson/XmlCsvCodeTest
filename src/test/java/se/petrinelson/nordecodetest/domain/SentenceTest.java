package se.petrinelson.nordecodetest.domain;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.stream.Collectors;

public class SentenceTest extends TestCase {

    @Test
    public void testRemovalOfSomCharachters() throws Exception {
        Sentence testSentence = new Sentence("Hello,you man, nice stuff.Hello you fact - in all of the Nordics, you’d , in this test the minus charachter is ..... removed");
        String expectedResult = "all,charachter,fact,Hello,Hello,in,in,is,man,minus,nice,Nordics,of,removed,stuff,test,the,the,this,you,you,you’d";
        String result = testSentence.getWords().stream().map(word -> word.getValue()).collect(Collectors.joining(","));
        assertEquals(result, expectedResult);
    }

    @Test
    public void testRemovalOfSungleMinusCharachter() throws Exception {
        Sentence testSentence = new Sentence("What\the  shouted was shocking:  停在那儿, 你这肮脏的掠夺者! I couldn't understand a word,perhaps because Chinese");
        String expectedResult = "a,because,Chinese,couldn't,he,I,perhaps,shocking,shouted,understand,was,What,word,你这肮脏的掠夺者!,停在那儿";
        String result = testSentence.getWords().stream().map(word -> word.getValue()).collect(Collectors.joining(","));
        assertEquals(result, expectedResult);
    }

    @Test
    public void testSentenceSplitInMultipleLines() throws Exception {
        Sentence testSentence = new Sentence("Mening rad1\n Mening rad2\n Mening rad3\n Mening rad4.");
        String expectedResult = "Mening,Mening,Mening,Mening,rad1,rad2,rad3,rad4";
        String result = testSentence.getWords().stream().map(word -> word.getValue()).collect(Collectors.joining(","));
        assertEquals(result, expectedResult);
    }

    @Test
    public void testSentenceOrder() throws Exception {
        Sentence testSentence = new Sentence("å ä ö q w r t y a b c d e f");
        String expectedResult = "a,b,c,d,e,f,q,r,t,w,y,ä,å,ö";
        String result = testSentence.getWords().stream().map(word -> word.getValue()).collect(Collectors.joining(","));
        assertEquals(result, expectedResult);
    }
}