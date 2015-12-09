package nl.mprog.hungman.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Parcel;
import android.os.Parcelable;
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

import nl.mprog.hungman.handler.XmlStringArrayHandler;

/**
 *
 * Gameplay superclass.
 * Programmatically represents everything needed for
 * a game of hangman.
 * It doesn't implement any specific gamemode or interaction with the user.
 *
 * @author Joost Bremmer
 * @since 1.0
 */

public abstract class Gameplay implements Parcelable{
    protected Context   context;
    protected ArrayList<String> wordList;
    private int wordMaxLength;
    private int   longestWord;
    protected String     word;
    private int         turns;
    private int         lives;
    private int         score;
    private StringBuilder guessedSoFar;
    private StringBuilder correctSoFar;
    public boolean gameWon;


    /**
     * Constructor class.
     * Gets the setting values, secret word, blindword, and guessedSoFar
     * @param context base context
     */
    public Gameplay(Context context) {
        //attributes we can init directly
        this.context = context;
        this.turns = 1;
        this.score = 100;
        this.wordList = new ArrayList<>();
        this.guessedSoFar = new StringBuilder();

        //method initialization
        readSettings();
        //set secret word
        this.word = fetchWord();
        Log.d("secret word", this.word);
        Log.d("Blindword", correctSoFar.toString());


    }

    /**
     * Constructor which gets the wordList from a parameter and does not need to read the XML file.
     * @param context base Activity context
     * @param wordList ArrayList<String> to use as list of possible words.
     */
    public Gameplay(Context context, ArrayList<String> wordList){
        //attributes we can set directly.
        this.context = context;
        this.turns = 1;
        this.score = 100;
        this.wordList = wordList;
        this.guessedSoFar = new StringBuilder();

        //method initialization
        readSettings();
        //set secret word
        this.word = fetchWord();
        calculateLongestWord();
        Log.d("secret word", this.word);
        Log.d("Blindword", correctSoFar.toString());

    }

    /**
     * Constructor using Parcel as parameter
     * @param in Parcel containing all the relevant values
     */
    protected Gameplay(Parcel in) {
        context = null;
        if (in.readByte() == 0x01) {
            wordList = new ArrayList<String>();
            in.readList(wordList, String.class.getClassLoader());
        } else {
            wordList = null;
        }
        wordMaxLength = in.readInt();
        longestWord = in.readInt();
        word = in.readString();
        turns = in.readInt();
        lives = in.readInt();
        score = in.readInt();

        guessedSoFar = new StringBuilder(in.readString());
        correctSoFar = new StringBuilder(in.readString());
        this.gameWon = in.readByte() != 0x00;
    }

    /**
     * Parcelable can't pass Context objects, so we'll have to remember to manually
     * reset it when parcelling Gameplay objects.
     * @param context the new context.
     */
    public void updateContext(Context context){
        this.context = context;
    }



    /**
     * Reads settings from SharedPreferences file and translates them to "Gameplay" settings
     *
     */
    public void readSettings(){
        //Fetch settings
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

        //Get individual values from settings.
        this.wordMaxLength = settings.getInt("wordMaxLength", 7);
        this.lives = settings.getInt("lives", 10);
        this.longestWord = settings.getInt("longestWord", 0);
    }


    /**
     * Fetches a random word from a database to use as the secret word
     */
    public String fetchWord() {
        if (this.wordList.isEmpty()) {
            readWordList(Constant.WORDSMALLFILE);
        }

        String secretword;

        Log.d("Wordlist size", String.valueOf(wordList.size()));
        if (wordList.size() == 1) {
            //Log.d("fetchword", "Only one word in list");
            secretword =  wordList.get(0);
        } else {

            //get random word
            Random rando = new Random();
            secretword = wordList.get(rando.nextInt(wordList.size()));

            //return randoWord once it is not the same as current word
            if (secretword.equals(this.word)) {
                secretword = fetchWord();
            }
        }

        initBlindWord(secretword);

        return secretword;

    }

    /**
     * Parse wordlist from an XML file using SAX XMLReader.
     * @param assetname path to the xml file in the assets folder.
     */
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
            XMLReader saxReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();

            //Initiate the XMLReader Handler
            XmlStringArrayHandler wordListHandler =
                    new XmlStringArrayHandler(wordMaxLength);


            //set handler and parse document
            saxReader.setContentHandler(wordListHandler);
            saxReader.setErrorHandler(wordListHandler);
            saxReader.parse(new InputSource(wordStream));
            timers.addSplit("parsing xml");
            timers.dumpToLog();

            wordList = wordListHandler.getStringArray();
            longestWord = wordListHandler.getLongestWord();



            //Check length
            Log.d("SAX list length", String.valueOf(wordList.size()));
            Log.d("SAX longest word", String.valueOf(longestWord));

        }
        catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    /**
     * Calculates the length of the longest word in wordList.
     * Used when Xml file is not read in class.
     */
    public void calculateLongestWord(){
        //Check each word for the longest possible length and save it to longestWord.
        for(String word: wordList){
            if(word.length() > longestWord){
                setLongestWord(word.length());
            }
        }
    }


    /**
     * @deprecated since 30-11-15, SAX is supposedly quicker,
     *             and resulting arraylist is reported as being twice as long as it should be.
     * @see #readWordList(String)
     *
     * Parses the wordlist from an XML file using the XmlPullParser
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

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the blindword (showing letters correctly guessed so far)
     * to be as long as the secret word and be filled with underscores
     * before any letters have been guessed.
     */
    public void initBlindWord(String secretWord){
        this.correctSoFar = new StringBuilder(secretWord.length());
        for(int i=0;i<secretWord.length();i++){
            correctSoFar.append("_");
        }
    }



    /**
     * effectively plays one turn, guessing a letter
     * Then updates the gamestate depending on the answer
     * @param letter Character, alphabetic to check against the secret word
     * @see #checkWord(Character)
     * @see #updateGuessedSoFar(Character)
     * @see #updateCorrectSoFar(Character)
     * @see #changeScore(int)
     *
     */
    public boolean guessLetter(Character letter){return false;}

    /**
     * Updates the letters already guessed
     * @return true if letter hasn't already been added, false if already guessed.
     */
    public boolean updateGuessedSoFar(Character letter) {

        //if letter hasn't already been guessed, add letter, return true
        if(!guessedSoFar.toString().contains(letter.toString())) {
            guessedSoFar.append(letter);
            //Log.d("Guessed so far", guessedSoFar.toString());
            return true;
        }
        else{
            //Log.d("Guessed so far", guessedSoFar.toString());
            return false;
        }


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
    public void updateCorrectSoFar(Character letter){

        for(int i=0;i<this.word.length();i++) {
            if(this.word.charAt(i) == letter){
                correctSoFar.setCharAt(i,letter);
            }
        }
    }


    /**
     * In- or decreases the score by a certain amount
     * @param amount integer, can be negative to decrease the score.
     */
    public void changeScore(int amount) {
        score += amount;
    }

    /**
     * In- or decreases the lives by a certain amount
     * @param amount integer, can be negative to decrease the lives left.
     */
    public void changeLives(int amount) {
        lives += amount;
    }



    /**
     * Checks game conditions. if game is won it'll flip this.won
     * @return True if game is over, false if not yet
     */
    public boolean gameOver(){
        if(this.lives == 0){
            this.gameWon = false;
            return true;
        }

        else if(correctSoFar.toString().matches(word)){
            this.gameWon = true;
            return true;
        }

        else {return false;}
    }

    /**
     * increase the turn timer by 1.
     */
    public void increaseTurnTimer(){this.turns++;}


    /* Parcelable methods */
    /**
     * Needed to implement Parcelable
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write all relevant data to a Parcel object
     * @param dest Parcel object that stores all relevant data
     * @param flags needed to implement parcelable, unused
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (wordList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(wordList);
        }
        dest.writeInt(wordMaxLength);
        dest.writeInt(longestWord);
        dest.writeString(word);
        dest.writeInt(turns);
        dest.writeInt(lives);
        dest.writeInt(score);

        dest.writeString(getGuessedSoFar());
        dest.writeString(getBlindWord());
        dest.writeByte((byte) (this.gameWon ? 0x01 : 0x00));
    }

    /* getters */

    /**
     * Return the amounts of lives left.
     * @return lives as int.
     */
    public int getLives(){return lives;}

    /**
     * returns the letters already guessed as a string.
     */
    public String getGuessedSoFar() {return guessedSoFar.toString();}

    /**
     * Returns the blindword (showing letters correctly guessed so far) as a String
     */
    public String getBlindWord() {return correctSoFar.toString();}


    /**
     * Returns the length of the longest word in wordList
     */
    public int getLongestWord(){return longestWord;}

    /**
     * Return the amount of turns played so far.
     */
    public int getTurns(){return this.turns;}


    /* setters */

    /**
     * Sets the length of the longest word in wordList
     * @param counter longest word so far
     */
    public void setLongestWord(int counter) {
        this.longestWord = counter;
    }


}
