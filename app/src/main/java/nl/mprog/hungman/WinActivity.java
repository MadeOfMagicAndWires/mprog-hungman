package nl.mprog.hungman;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class WinActivity extends HungmanActivity {

    private Gameplay gameInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button buttan = (Button) findViewById(R.id.goback);
        buttan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        if(intent.hasExtra("gameState")) {
            gameInstance = intent.getParcelableExtra("gameState");
            gameInstance.updateContext(this);
            String winstatement = gameInstance.gameWon ?  getString(R.string.game_won) :
                    getString(R.string.game_lost);
            updateTextView(R.id.winstatement, winstatement, EditMode.NONE);
        }


    }

}
