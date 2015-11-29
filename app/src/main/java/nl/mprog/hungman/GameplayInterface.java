package nl.mprog.hungman;

import android.content.Context;
import android.util.Pair;

import java.nio.CharBuffer;
import java.util.ArrayList;

/**
 * @deprecated 29-11-15
 * Gameplay interface used by NeutralGameplay
 *
 */

public interface GameplayInterface {

    /**
     * Reads settings from SharedPreferences file and translates them to "Gameplay" settings
     *
     */
    public void readSettings();


    /**
     * Fetches a random word from a database to use as the secret word
     */
    public String fetchWord();

    /**
     * Reads the wordlist from the Resource R.values.wordlist
     */
    public void readWordList();

    /**
     * Fetches the highscores from different playsessions
     */
    public ArrayList<Integer> readHighscores();


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
    public void guessLetter(Character letter);

    /**
     * Updates the letters already guessed
     */
    public void updateGuessedSoFar(Character letter);

    /**
     * Checks if the Charsequence contains any instnces of a specific letter
     * @param letter
     * @return True or False, depending on whether the letter was found in the word
     */
    public boolean checkWord(Character letter);

    /**
     * Updates the letters that were correctly guessed so far
     */
    public void updateCorrectSoFar(Character letter, int position);


    /**
     * In- or decreases the score by a certain amount
     */
    public void changeScore(int amount);

    /**
     * Checks if the current score is  a new highscore
     */
    public boolean checkHighscrores();

    /**
     * Writes high score back to file.
     * @return
     */
    public boolean writeHighscores();
}
