package nl.mprog.hungman;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.util.TimingLogger;
import android.util.Xml;
import android.widget.ArrayAdapter;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Random;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * Gameplay superclass.
 * Programmatically represents everything needed for
 * a game of hangman.
 * It doesn't implement any specific gamemode or interaction with the user.
 *
 */

public abstract class Gameplay {

    static public final String HIGHSCOREFILE = "highscores.csv";
    static public final String WORDLISTFILE  = "words.xml";
    static public final String WORDSMALLFILE = "small.xml";

    private Context   context;
    private ArrayList<String> wordList;
    private int    wordMaxLength;
    private int    longestWord;
    private String       word;
    private int         turns;
    private int         lives;
    private int         score;
    private ArrayList<Pair> highscores;
    private CharBuffer guessedSoFar;
    private StringBuilder correctSoFar;
    private boolean won;


    /**
     * Constructor class.
     * Gets the secret word, highscores, and guessedSoFar
     */
    public Gameplay(Context context) {
        //attributes we can init directly
        this.context = context;
        this.turns = 1;
        this.score = 100;
        this.wordList = new ArrayList<>();

        //init methods
        readSettings();

        //get word
        this.word = fetchWord();
        Log.d("secret word", this.word);

        //set blindword
        initCorrectSoFar();
        Log.d("Blindword", correctSoFar.toString());


    }

    /**
     * Constructor which sets the secret word to a specific word
     * @param word String to use as the word to be guessed
     */
    public Gameplay(Context context, String word) {
        this.context = context;
        this.turns = 1;
        this.score = 100;
        this.word = word;
        Log.d("Secret word", this.word);

    }


    /**
     * Reads settings from SharedPreferences file and translates them to "Gameplay" settings
     *
     */
    public void readSettings(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        this.wordMaxLength = settings.getInt("wordMaxLength", 7);
        this.lives = settings.getInt("lives", 10);
        this.longestWord = settings.getInt("longestWord", 0);
    }


    /**
     * Fetches a random word from a database to use as the secret word
     */
    public String fetchWord() {
        if(this.wordList.isEmpty()){
            readWordList(WORDLISTFILE);
        }

        //get random word
        Random rando = new Random();
        String randoWord = wordList.get(rando.nextInt(wordList.size()));

        //return randoWord once it is not the same as current word
        if(!randoWord.equals(word)) {
            return randoWord;
        } else { return fetchWord(); }

    }

    public void readWordList(String assetname) {
        this.wordList.clear();
        setLongestWord(0);

        TimingLogger timers = new TimingLogger("Hungman", "readWordList");
        try{
            //initiate file stream
            InputStream wordStream = context.getAssets().open(assetname);

            //Initiate wordbuffer
            final StringBuilder builder = new StringBuilder();

            //initiate the parser, with custom handler WordListHandler
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader saxReader = parser.getXMLReader();

            //Initiate the XMLReader Handler
            DefaultHandler wordListHandler = new DefaultHandler(){
                boolean parsing = false;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
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
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (localName.equals("item")) {

                        if(builder.length() > longestWord){
                            setLongestWord(builder.length());
                        }

                        if (builder.length() <= wordMaxLength) {
                            wordList.add(builder.toString());
                            //Log.d("Added word", builder.toString());
                        }

                        builder.setLength(0);
                        parsing = false;
                    }
                }

            };

            //set handler and parse document
            saxReader.setContentHandler(wordListHandler);
            saxReader.setErrorHandler(wordListHandler);
            saxReader.parse(new InputSource(wordStream));
            timers.addSplit("parsing xml");
            timers.dumpToLog();

            //Check length
            Log.d("SAX list length", String.valueOf(wordList.size()));
            Log.d("SAX longest word", String.valueOf(longestWord));

        }
        catch (SAXException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sets the number for the longest word counter.
     * @param counter longest word so far
     */
    public void setLongestWord(int counter) {
        this.longestWord = counter;
    }


    /**
     * @deprecated 30-11-15
     * Reads the wordlist from a file
     * @param assetname filename to read
     */
    public void readWordListPull (String assetname) {
        wordList.clear();
        setLongestWord(0);
        int wordcount = 0;

        try {
            //initiate xml parser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            //initiate file stream
            InputStream wordStream = context.getAssets().open(assetname);
            parser.setInput(wordStream, "UTF-8");

            int event = parser.getEventType();

            while(event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        String currentWord = parser.getText();
                        int currWordLength = currentWord.length();

                        if(currWordLength > longestWord) {
                            setLongestWord(currWordLength);
                        }

                        if(currWordLength <= wordMaxLength) {
                            wordcount++;
                            wordList.add(currentWord);
                            //Log.d("new word", currentWord);
                        }

                        break;

                    default:
                        break;

                }
                event = parser.next();
            }

            //Check list and longest word
            Log.d("XmlPull List length", String.valueOf(wordList.size()));
            Log.d("XmlPull Counter length", String.valueOf(wordcount));
            Log.d("Longest word in list", String.valueOf(longestWord));

            // Needed this to check what was wrong with xmlPullParser
            // it adds more (empty?) items to the ArrayList than necessary ??
            //for(int i=0;i<wordList.size();i++) {
            //    Log.d("Word "+i, wordList.get(i));
            //}

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void initCorrectSoFar(){
        this.correctSoFar = new StringBuilder(word.length());
        for(int i=0;i<word.length();i++){
            correctSoFar.append("_");
        }
    }


    /**
     * Fetches the highscores from different playsessions
     */
    static public ArrayList<Integer> readHighscores(){return null;}


    /**
     * effectively plays one turn, guessing a letter
     * Then updates the gamestate depending on the answer
     * @param letter Character, alphabetic to check against the secret word
     * @see #checkWord(Character)
     * @see #updateGuessedSoFar(Character)
     * @see #updateCorrectSoFar(Character, int)
     * @see #changeScore(int)
     *
     */
    public void guessLetter(Character letter){
        updateGuessedSoFar(letter);
        if(checkWord(letter)){
            for(int i=0;i<word.length();i++) {
                if(word.charAt(i) == letter){
                    updateCorrectSoFar(letter,i);
                }
            }

        }
        else {
            lives -= 1;
            score -= 10;
        }
    }

    /**
     * Updates the letters already guessed
     */
    public void updateGuessedSoFar(Character letter){
        guessedSoFar.append(letter);
    }

    /**
     * Checks if the Charsequence contains any instnces of a specific letter
     * @param letter character to check against, must be a-Z
     * @return True or False, depending on whether the letter was found in the word
     */
    public boolean checkWord(Character letter){
        return word.contains(String.valueOf(letter));
    }


    /**
     * Updates the letters that were correctly guessed so far
     */
    public void updateCorrectSoFar(Character letter, int position){
        correctSoFar.replace(position,position, String.valueOf(letter));

    }

    /**
     * In- or decreases the score by a certain amount
     */
    public void changeScore(int amount) {
        score += amount;
    }

    public boolean gameOver(){return false;}


    /**
     * Checks if the current score is  a new highscore
     */
    public boolean checkHighscores(){
        return false;
    }

    /**
     * Writes high score back to file.
     * @return true if successful
     */
    public boolean writeHighscores() {
        return false;
    }
}
