package nl.mprog.hungman.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/**
 * EvilGameplay is a subclass of Gameplay implementing playing the **Evil** version of Hangman.
 *
 * @author Joost Bremmer
 * @since 1.5
 */
public class EvilGameplay extends Gameplay {

    /**
     * {@inheritDoc}
     */
    public EvilGameplay(Context context){
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    public EvilGameplay(Context context, ArrayList<String> wordList){
        super(context,wordList);
    }

    /**
     * Constructor used by Parcelable
     * @param in parcel containing data.
     */
    public EvilGameplay(Parcel in) {
        super(in);
    }



    /**
     * Plays a single turn using EvilGameplay rules, then updates the gamestate
     * @see #checkWordList(Character)
     * */
    @Override
    public boolean guessLetter(Character letter) {
        //we'll return this later.
        boolean guessWasCorrect;

        //add letters to the guessed so far.
        updateGuessedSoFar(letter);
        /*
         * Check if wordlist has more words with letter in it,
         * if not bump down score and lives
         */
        if(checkWordList(letter)){
            guessWasCorrect = true;
        }
        else {
            changeLives(-1);
            changeScore(-10);
            guessWasCorrect = false;
        }

        //increase turntimer.
        increaseTurnTimer();
        return guessWasCorrect;

    }

    /**
     * Check a letter against the entire wordlist, rather than against a single word,
     * then updates the wordlist to the subset it needs to keep.
     *
     * @param letter letter to check
     * @return true if there are more words *containing* the letter then there are without,
     *         false if otherwise.
     *
     * @see #updateWordList(ArrayList)
     */
    public boolean checkWordList(Character letter){
        //we'll return this later.
        boolean guessWasCorrect = false;

        //init subset lists. One for the words containing the letters and one for those who don't.
        ArrayList<String> containsLetter = new ArrayList<>();
        ArrayList<String> doesnotcontainLetter = new ArrayList<>();

        //Add every current word to their respective subsets.
        for (String currentword: wordList
             ) {
            if(currentword.contains(letter.toString())) {
                containsLetter.add(currentword);
            }
            else {
                doesnotcontainLetter.add(currentword);
            }

        }

        /*
         * Check whichever subset list is bigger, and set that as the new word list.
         * If the subset containing the letter was bigger, set guessWasCorrect to true.
         */
        if (containsLetter.size() > doesnotcontainLetter.size()){
            //Log.d("success!", "more words with " + letter.toString());
            updateWordList(containsLetter);
            guessWasCorrect = true;

        }
        else {
            //Log.d("too bad!", "more words without " + letter.toString());
            updateWordList(doesnotcontainLetter);
        }

        //Log.d("containsLetter size", String.valueOf(containsLetter.size()));
        //Log.d("!containsLetter size", String.valueOf(doesnotcontainLetter.size()));
        return guessWasCorrect;
    }

    /**
     * Replaces the wordlist currently kept to a new one.
     * @param newWordList new wordlist to keep.
     */
    public void updateWordList(ArrayList<String> newWordList) {
        //update wordlist
        this.wordList = newWordList;
        Log.d(" new wordlist", wordList.toString());
        Log.d("New Wordlist length", String.valueOf(wordList.size()));

        //If the previous secretword is not in the new wordlist, get a new secret word.
        if(!wordList.contains(this.word)){
            this.word = fetchWord();
            //Log.d("secret word", this.word);
        }

        //Update the blindword.
        String guessed = getGuessedSoFar();
        for(int i=0;i<guessed.length();i++) {
            updateCorrectSoFar(guessed.charAt(i));
        }

    }

    /**
     * Used by Parcelable
     */
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Gameplay> CREATOR = new Parcelable.Creator<Gameplay>() {
        @Override
        public EvilGameplay createFromParcel(Parcel in) {
            return new EvilGameplay(in);
        }

        @Override
        public EvilGameplay[] newArray(int size) {
            return new EvilGameplay[size];
        }
    };

}
