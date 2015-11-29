package nl.mprog.hungman;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.util.Xml;
import android.widget.ArrayAdapter;

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

        //init methods
        readSettings();
        readWordList(WORDLISTFILE);
        this.word = fetchWord();
        Log.v("secret word", this.word);
    }

    /**
     * Constructor which sets the secret word to a specific word
     * @param word String to use as the word to be guessed
     */
    public Gameplay(Context context, String word) {

    }

    /**
     * Reads settings from SharedPreferences file and translates them to "Gameplay" settings
     *
     */
    public void readSettings(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        this.wordMaxLength = settings.getInt("wordMaxLength", 7);
        this.lives = settings.getInt("lives", 10);
    }


    /**
     * Fetches a random word from a database to use as the secret word
     */
    public String fetchWord() {

        //get random word
        Random rando = new Random();
        String randoWord = wordList.get(rando.nextInt(wordList.size()));

        //return randoWord once it is not the same as current word
        if(!randoWord.equals(word)) {
            return randoWord;
        } else { return fetchWord(); }

    }


    /**
     * Reads the wordlist from a file
     * @param assetname filename to read
     */
    public void readWordList (String assetname) {
        wordList = new ArrayList<>();
        try {
            //initiate xml parser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            //initiate file stream
            InputStream wordStream = context.getAssets().open(assetname);
            parser.setInput(wordStream, "UTF-8");

            int event = parser.getEventType();
            int longestWord = 0;

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
                            longestWord = currWordLength;
                        }

                        if(currWordLength <= wordMaxLength) {
                            wordList.add(currentWord);
                            Log.d("new word", currentWord);
                        }

                        break;

                    default:
                        break;

                }
                event = parser.next();
            }

            Log.d("Longest word in wordlist", String.valueOf(longestWord));

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

    }

    /**
     * Updates the letters already guessed
     */
    public void updateGuessedSoFar(Character letter){

    }

    /**
     * Checks if the Charsequence contains any instnces of a specific letter
     * @param letter character to check against, must be a-Z
     * @return True or False, depending on whether the letter was found in the word
     */
    public boolean checkWord(Character letter){
        return false;
    }


    /**
     * Updates the letters that were correctly guessed so far
     */
    public void updateCorrectSoFar(Character letter, int position){

    }


    /**
     * In- or decreases the score by a certain amount
     */
    public void changeScore(int amount) {

    }

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
