package nl.mprog.hungman;

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

    private String word;
    private int         turns;
    private int   guessesLeft;
    private int         score;
    private LinkedList<Integer> highscores;
    private CharBuffer guessedSoFar;
    private StringBuilder correctSoFar;
    private boolean won;

    /**
     * Constructor class.
     * Gets the secret word, highscores, and guessedSoFar
     */
    public void Gameplay() {


    }

    /**
     * Constructor which sets the secret word to a specific word
     * @param word String to use as the word to be guessed
     */
    public void Gameplay(String word){

    }

    /**
     * Fetches a random word from a database to use as the secret word
     */
    public String fetchWord() {

    }

    /**
     * Fetches the highscores from different playsessions
     */
    public ArrayList<Integer> fetchHighScores(){

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

}
