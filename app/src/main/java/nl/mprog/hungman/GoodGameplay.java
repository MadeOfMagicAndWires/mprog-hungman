package nl.mprog.hungman;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import java.nio.CharBuffer;
import java.util.ArrayList;

/**
 * Subclass of Gameplay implementing the good (standard) version of Hangman.
 */
public class GoodGameplay extends Gameplay {



    public GoodGameplay(Context context) {
        super(context);
    }

    @Override
    public boolean guessLetter(Character letter) {

        boolean guessWasCorrect;
        updateGuessedSoFar(letter);

        if(checkWord(letter)){
            for(int i=0;i<this.word.length();i++) {
                if(this.word.charAt(i) == letter){
                    updateCorrectSoFar(letter,i);
                }
            }
            guessWasCorrect = true;

        }
        else {
            changeLives(-1);
            changeScore(-10);
            guessWasCorrect = false;
        }

        if(gameOver()) {
            if(gameWon) {
                Log.v("Congratulations", context.getString(R.string.game_won));
            }
            else {
                Log.v("Congratulations", "You lost!");
            }

        }

        return guessWasCorrect;

    }
}
