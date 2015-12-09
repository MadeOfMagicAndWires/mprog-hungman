package nl.mprog.hungman.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Subclass of Gameplay implementing playing the good (standard) version of Hangman.
 *
 * @author Joost Bremmer
 * @since  1.0
 */
public class GoodGameplay extends Gameplay{


    /**
     * {@inheritDoc}
     */
    public GoodGameplay(Context context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    public GoodGameplay(Context context, ArrayList<String> wordList){
        super(context,wordList);
    }


    /**
     * Constructor used by Parcelable
     * @param in parcel containing data.
     */
    public GoodGameplay(Parcel in) {
        super(in);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean guessLetter(Character letter) {
        boolean guessWasCorrect;

        //update the guessed letters
        updateGuessedSoFar(letter);

        //check if letter is in word
        // if not lower lives and score
        if(checkWord(letter)){
            updateCorrectSoFar(letter);
            guessWasCorrect = true;
        }
        else {
            changeLives(-1);
            changeScore(-10);
            guessWasCorrect = false;
        }

        //increase turns
        increaseTurnTimer();
        return guessWasCorrect;

    }

    /**
     * Used by Parcelable
     */
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Gameplay> CREATOR = new Parcelable.Creator<Gameplay>() {
        @Override
        public GoodGameplay createFromParcel(Parcel in) {
            return new GoodGameplay(in);
        }

        @Override
        public GoodGameplay[] newArray(int size) {
            return new GoodGameplay[size];
        }
    };


    /**
     * Overwrite writeToParcel method to not send wordList.
     * Since the wordList is never wittled down in GoodGameplay,
     * it becomes too big to put in a bundle and send on to WinActivity.
     *
     * @see Gameplay#writeToParcel(Parcel, int)
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        this.wordList = null;
        super.writeToParcel(dest, flags);
    }

}
