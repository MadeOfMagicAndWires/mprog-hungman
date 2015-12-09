package nl.mprog.hungman.interfaces;

import android.content.Context;
import android.util.Pair;

import java.nio.CharBuffer;
import java.util.ArrayList;

import nl.mprog.hungman.GameActivity;

/**
 * @deprecated since 29-11-15,
 *             rendered unnecessary. Left in for prosperity.
 * @see GameActivity#initGameplay()
 *
 * Gameplay interface used by NeutralGameplay
 *
 * @author Joost Bremmer
 * @since  0.5
 */

public interface GameplayInterface {

    /**
     * Reads settings from SharedPreferences file and translates them to "Gameplay" settings
     *
     */
    void readSettings();


    /**
     * Fetches a random word from a database to use as the secret word
     */
    String fetchWord();

    /**
     * Reads the wordlist from the Resource R.values.wordlist
     */
    void readWordList(String assetname);


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
    void guessLetter(Character letter);

    /**
     * Updates the letters already guessed
     */
    void updateGuessedSoFar(Character letter);

    /**
     * Checks if the Charsequence contains any instnces of a specific letter
     * @param letter letter to check against
     * @return True or False, depending on whether the letter was found in the word
     */
    boolean checkWord(Character letter);

    /**
     * Updates the letters that were correctly guessed so far
     */
    void updateCorrectSoFar(Character letter);


    /**
     * In- or decreases the score by a certain amount
     */
    void changeScore(int amount);


}
