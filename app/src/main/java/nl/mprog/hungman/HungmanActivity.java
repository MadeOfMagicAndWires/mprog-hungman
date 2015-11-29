package nl.mprog.hungman;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Basic Activity
 */
public class HungmanActivity extends AppCompatActivity {



    static public final List<Pair> DEFAULTCFG = Collections.unmodifiableList(
            new ArrayList<Pair>() {{
                add(Pair.create("wordlength", 7));
                add(Pair.create("lives", 10));
                add(Pair.create("good", true));
            }});

    protected SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readSettings();

    }

    public void readSettings() {

        //Haal de app settings op
        settings = PreferenceManager.getDefaultSharedPreferences(this);

        //Check of settings leeg is (heeft geen setting die er sowieso in moet zitten)
        //Zo niet, voeg dan alle standaard settings in DEFAULTCFG toe.
        if(!settings.contains("wordlength")) {
            //maak editor aan
            SharedPreferences.Editor settingsEditor = settings.edit();
            //Ga alle default settings in DEFAULTCFG af, en voeg ze toe.
            for (int i = 0; i < DEFAULTCFG.size(); i++) {
                Pair setting = DEFAULTCFG.get(i);
                switch (setting.second.getClass().getSimpleName()) {
                    case "Integer":
                        settingsEditor.putInt((String) setting.first, (Integer) setting.second);
                        break;
                    case "Boolean":
                        settingsEditor.putBoolean((String) setting.first, (Boolean) setting.second);
                        break;
                    default:
                        Log.e("Default settings error", "Onbekende setting:" + setting.first);
                        break;
                }

            }
            settingsEditor.commit();
            Log.v("readSettings", "Default settings aangemaakt");
        }

    }

}
