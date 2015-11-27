package nl.mprog.hungman;

import android.content.Context;
import android.util.Pair;
import android.widget.ArrayAdapter;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * Gameplay superclass.
 * Programmatically represents everything needed for
 * a game of hangman.
 * It doesn't implement any specific gamemode or interaction with the user.
 *
 */

public abstract class Gameplay {

    private Context   context;
    private int wordListResource;
    private ArrayList<String> wordList;
    private int    wordLength;
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
    public void Gameplay(Context context) {


    }

    /**
     * Constructor which sets the secret word to a specific word
     * @param word String to use as the word to be guessed
     */
    public void Gameplay(Context context, String word){

    }

    /**
     * Reads settings from SharedPreferences file and translates them to "Gameplay" settings
     *
     */
    public void readSettings(){

    }


    /**
     * Fetches a random word from a database to use as the secret word
     */
    public String fetchWord() {
        return new String();
    }


    /**
     * Reads the wordlist from the Resource R.values.wordlist
     */
    public void readWordList() {

    }


    /**
     * Fetches the highscores from different playsessions
     */
    public ArrayList<Integer> readHighscores(){
        return new ArrayList<Integer>();

    }


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
     * @param letter
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
    public boolean checkHighscrores(){
        return false;
    }

    /**
     * Writes high score back to file.
     * @return
     */
    public boolean writeHighscores() {
        return false;
    }
}
