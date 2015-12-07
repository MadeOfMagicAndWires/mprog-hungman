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
 *
 * See also:
 * <a href="https://developer.android.com/reference/org/xml/sax/helpers/DefaultHandler.html">
 *     Google Documentation on SAXParser in Android
 * </a>
 * for more information.
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


    /* getters */

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


    /* setters */

    /**
     * updates the counter of the longest word length in stringArray.
     * @param count new count of the longest word.
     */
    public void setLongestWord(int count) {
        this.longestWord = count;
    }


    /* Inherited required classes */


    /**
     * This method fires whenever the SAX parser encounters a new element.
     * @param uri possible namespace uri, or empty
     * @param localName element name without "whatever:" prefix
     * @param qName other element name, including "whatever:" prefix
     * @param attributes any possible attributes, empty if none
     * @throws SAXException
     *
     * @see DefaultHandler#startElement(String, String, String, Attributes)
     */
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //if current element is <item> save it's value.
        if (localName.equals("item")) {
            parsing = true; //tells characters() to start paying attention
        }
    }

    /**
     * This method fires for all content **between** elements i.e. an elements value.
     * @param chars element's value as char[]
     * @param i     start of chars array
     * @param i1    end of chars array
     * @throws SAXException Any SAXException
     */
    @Override
    public void characters(char[] chars, int i, int i1) throws SAXException {
        //If parsing is set to true by startElement, save the value of the element
        //to the StringBuilder buffer.
        if (parsing) {
            builder.append(new String(chars, i, i1));
        }
    }

    /**
     * This method fires when the SAX parser reaches the **end** of an element
     * @param uri possible namespace
     * @param localName element name without "whatever:" prefix
     * @param qName element name with "whatever:" prefix
     * @throws SAXException Any SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        //Check whether the current element is </item>
        if (localName.equals("item")) {

            //check if the value currently saved is longer than the record so far.
            //if so, overwrite it with the current length.
            if(builder.length() > getLongestWord()){
                setLongestWord(builder.length());
                //Log.d("Added word", builder.toString());
            }

            //If we didn't set a maximum word length add every word to the list
            if (wordMaxLength == 0) {
                stringArray.add(builder.toString());
            }
            //Otherwise, only add the word if it's within the maximum word length limit.
            else if (this.wordMaxLength != 0 && builder.length() <= wordMaxLength) {
                stringArray.add(builder.toString());
                //Log.d("Added word", builder.toString());
            }

            //Afterwards reset the StringBuilder buffer and parsing boolean.
            builder.setLength(0);
            parsing = false;
        }
    }



}
