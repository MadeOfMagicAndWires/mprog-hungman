package nl.mprog.hungman;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * @deprecated since 29-11-15.
 *             Proved Completely unecessary, as Java apparently doesn't recast subclasses.
 * @see GameActivity#initGameplay()
 *
 * NeutralGamplay is used as a bridge between GameActivity and Gameplay classes
 * Since GameActivity initially doesn't know which Gameplay class to use it will create
 * a NeutralGameplay object, which will patch any command through
 * to whichever Gameplay instance is needed.
 *
 * @author Joost Bremmer
 * @since  0.5
 */

public class NeutralGameplay implements GameplayInterface {

    private Context context;
    private GoodGameplay goodGameInstance;
    private EvilGameplay evilGameInstance;
    private boolean good;


    public NeutralGameplay(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

        this.good = settings.getBoolean("good", true);

        if(good) {
            goodGameInstance = new GoodGameplay(context);
        } else { evilGameInstance = new EvilGameplay(context); }
    }

    /**
     * Reads settings from SharedPreferences file and translates them to "Gameplay" settings
     *
     */
    public void readSettings(){

        if(good) {
            goodGameInstance.readSettings();
        } else { evilGameInstance.readSettings(); }
    }

    /**
     * Fetches a random word from a database to use as the secret word
     */
    public String fetchWord() {
        return  good ? goodGameInstance.fetchWord() : evilGameInstance.fetchWord();
    }


    /**
     * Reads the wordlist from the Resource R.values.wordlist
     */
    public void readWordList(String assetname) {
        if(good) {
            goodGameInstance.readWordList(assetname);
        }
        else {
            evilGameInstance.readWordList(assetname);
        }

    }


    /**
     * Fetches the highscores from different playsessions
     */
    public ArrayList<Integer> readHighscores(){
        return Gameplay.readHighscores();
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
    public void guessLetter(Character letter){
        if(good) {
            goodGameInstance.guessLetter(letter);
        }
        else
        {
            evilGameInstance.guessLetter(letter);
        }
    }


    /**
     * Updates the letters already guessed
     */
    public void updateGuessedSoFar(Character letter){
        if(good) {
            goodGameInstance.updateGuessedSoFar(letter);
        }
        else
        {
            evilGameInstance.updateGuessedSoFar(letter);
        }

    }

    /**
     * Checks if the Charsequence contains any instances of a specific letter
     * @param letter character to check against, must be a-Z
     * @return True or False, depending on whether the letter was found in the word
     */
    public boolean checkWord(Character letter){
        return good ? goodGameInstance.checkWord(letter) : evilGameInstance.checkWord(letter);
    }


    /**
     * Updates the letters that were correctly guessed so far
     */
    public void updateCorrectSoFar(Character letter){
        if(good) {
            goodGameInstance.updateCorrectSoFar(letter);
        }
        else {
            evilGameInstance.updateCorrectSoFar(letter);
        }
    }


    /**
     * In- or decreases the score by a certain amount
     */
    public void changeScore(int amount) {
        if(good) {
            goodGameInstance.changeScore(amount);
        }
        else {
            evilGameInstance.changeScore(amount);
        }

    }

    /**
     * Checks if the current score is  a new highscore
     */
    public boolean checkHighscores(int newscore){
        return good ? goodGameInstance.checkHighscores(newscore)
                : evilGameInstance.checkHighscores(newscore);
    }

    /**
     * Writes high score back to file.
     * @return true if successful.
     */
    public boolean writeHighscores() {
        return good ? goodGameInstance.writeHighscores() : evilGameInstance.writeHighscores();
    }

}
