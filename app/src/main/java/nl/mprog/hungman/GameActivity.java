package nl.mprog.hungman;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;



public class GameActivity extends HungmanActivity{

    private Gameplay gameInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guessLetterListener();
            }
        });


        TextView.OnEditorActionListener editTextListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return guessLetterListener();
            }
        };

        EditText get = (EditText) findViewById(R.id.submit_letter);
        get.setOnEditorActionListener(editTextListener);


        initGameplay();
        gameInstance.fetchWord();
        updateUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Intent openSettings = new Intent(this, SettingsActivity.class);
                startActivity(openSettings);

            case R.id.action_newgame:
                initGameplay();
                updateUi();
                return true;

            case R.id.action_quit:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void initGameplay() {
        boolean good = settings.getBoolean("good", true);

        if (good) {
            gameInstance = new GoodGameplay(this);
        } else {
            gameInstance = new EvilGameplay(this);
        }

        Log.v("gameplay instance is", gameInstance.getClass().getName());
    }


    /**
     * Updates the relevant views on the UI to represent the current game state.
      */
    public void updateUi() {
        updateTextView(R.id.blindword, gameInstance.getBlindWord(), EditMode.ADDSPACING);
        updateTextView(R.id.guessedletters, gameInstance.getGuessedSoFar(), EditMode.ADDSPACING);

        int livesTotal = settings.getInt("lives", 7);
        int livesLeft = gameInstance.getLives();
        StringBuilder healthbar = new StringBuilder();
        for(int i=0;i<livesLeft;i++) {
            healthbar.append('\u2764');
        }

        for(int i=0;i<(livesTotal - livesLeft);i++) {
            healthbar.append('\u2661');
        }


        updateTextView(R.id.healthbar, healthbar.toString(),EditMode.NONE);

    }


    public boolean guessLetterListener() {
        EditText letterView = (EditText) findViewById(R.id.submit_letter);
        //because of the try error we need to initialise letter to something non-alphabetic.
        char letter = 0;
        boolean correct = false;

        Log.v("Input:", letterView.getText().toString());

        //if input is empty, notify user
        //can't use .isempty because of shitty minimum SDK.
        if (letterView.getText().toString() == "") {
            Snackbar.make(letterView, R.string.noletterinput, Snackbar.LENGTH_SHORT).show();
            Log.d("InputError", "Input is empty");
            return false;
        }
        else {
            //Get first character of input

            //This try is needed because Android is being difficult when we clear
            //the EditText later, throwing a StringIndexOutOfBoundsError because now .getText()
            //is empty so it can't use charAt(0).
            //This however is stupid, because WE already checked for an empty string.
            //We've already done everything we needed anyway once we cleared the text, so we can
            //just ignore it.
            try {
                letter = letterView.getText().toString().toUpperCase().charAt(0);
            } catch (StringIndexOutOfBoundsException e) {
                //do nothing
            }
        }


        // Check if character is alphabetic, if yes, fire guessLetter(), if not silently ignore.
        if (Character.isLetter(letter)) {
            correct = gameInstance.guessLetter(letter);
            Log.d("guess Correct?", String.valueOf(correct));
            updateUi();

        }


        letterView.getText().clear();
        return correct;
    }


}
