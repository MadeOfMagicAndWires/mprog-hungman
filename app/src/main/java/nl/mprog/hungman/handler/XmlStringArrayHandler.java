package nl.mprog.hungman.handler;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Handler class for parsing xml documents for string arrays
 * Parses xml for <item></item> elements and adds them to
 * an ArrayList<String>
 */
public class XmlStringArrayHandler extends DefaultHandler {

    private ArrayList<String> stringArray;
    private boolean parsing;
    private final StringBuilder builder = new StringBuilder();
    private int wordMaxLength;
    private int longestWord;

    /**
     * Constructor without maximum word length
     */
    public XmlStringArrayHandler() {
        this.stringArray = new ArrayList<String>();
        this.wordMaxLength = 0;
        this.longestWord = 0;
    }

    /**
     * Constructor with maximum word length, won't add words below the limit.

     * @param wordMaxLength maximum wordlength.
     */
    public XmlStringArrayHandler(int wordMaxLength){
        this.stringArray = new ArrayList<String>();
        this.wordMaxLength = wordMaxLength;
        this.longestWord = 0;
    }


    //getters

    /**
     * return stringArray as ArrayList
     */
    public ArrayList<String> getStringArray() {
        return stringArray;
    }

    /**
     * return longest word counter
     */
    public int getLongestWord() {
        return longestWord;
    }
    //setters

    /**
     * updates the counter of the longest word length in stringArray.
     * @param count new count of the longest word.
     */
    public void setLongestWord(int count) {
        this.longestWord = count;
    }


    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        if (localName.equals("item")) {
            parsing = true;
        }
    }

    @Override
    public void characters(char[] chars, int i, int i1) throws SAXException {
        if (parsing) {
            builder.append(new String(chars, i, i1));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (localName.equals("item")) {

            if(builder.length() > getLongestWord()){
                setLongestWord(builder.length());
                //Log.d("Added word", builder.toString());
            }

            if (wordMaxLength == 0) {
                stringArray.add(builder.toString());
            }
            else if (this.wordMaxLength != 0 && builder.length() <= wordMaxLength) {
                stringArray.add(builder.toString());
                //Log.d("Added word", builder.toString());
            } else {
                //do nothing
            }


            builder.setLength(0);
            parsing = false;
        }
    }



}
