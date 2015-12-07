package nl.mprog.hungman;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Hungman Activity superclass
 * implements methods to easily read and check SharedPreference settings
 * and update the text for various Views containing Text.
 *
 * @author Joost Bremmer
 * @since  1.0
 *
 */
public class HungmanActivity extends AppCompatActivity {
    static public final List<Pair> DEFAULTCFG = Collections.unmodifiableList(
            new ArrayList<Pair>() {{
                add(Pair.create("wordMaxLength", 7));
                add(Pair.create("lives", 10));
                add(Pair.create("evil", true));
            }});

    public enum EditMode {
        NONE,
        ADDSPACING,
    }

    public static final String WORDLIST = "wordList";
    public static final String GAMESTATE = "gameState";

    protected SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSettings();

    }

    public void initSettings() {

        //Haal de app settings op
        settings = PreferenceManager.getDefaultSharedPreferences(this);

        //Check of settings leeg is (heeft geen setting die er sowieso in moet zitten)
        //Zo niet, voeg dan alle standaard settings in DEFAULTCFG toe.
        String firstsetting = (String) DEFAULTCFG.get(0).first;
        if(!settings.contains(firstsetting)) {
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

    /**
     * update the text of any View that has a text attribute
     * @param ViewId int containing the id of target view, can be R.id.view
     * @param text text to put in View.
     * @param EditMode; EditMode.ADDSPACING or NONE if nothing should be edited.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void updateTextView(int ViewId, String text, EditMode EditMode) {
        TextView tv = (TextView) findViewById(ViewId);
        if (EditMode == HungmanActivity.EditMode.ADDSPACING
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            tv.setLetterSpacing(1);
        } else if (EditMode == HungmanActivity.EditMode.ADDSPACING) {
            text = addSpacing(text);
        }

        tv.setText(text);
    }

    /**
     * update the text of any View that has a text attribute
     * @param tv int containing the id of target view, can be R.id.view
     * @param text text to put in View.
     * @param EditMode; EditMode.ADDSPACING or NONE if nothing should be edited.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void updateTextView(TextView tv, String text, EditMode EditMode) {
        if (EditMode == HungmanActivity.EditMode.ADDSPACING
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tv.setLetterSpacing(1);
        } else if (EditMode == HungmanActivity.EditMode.ADDSPACING) {
            text = addSpacing(text);
        }

        tv.setText(text);
    }

    /**
     * update the text of any View that has a text attribute
     * @param et int containing the id of target view, can be R.id.view
     * @param text text to put in View.
     * @param EditMode; EditMode.ADDSPACING or NONE if nothing should be edited.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void updateTextView(EditText et, String text, EditMode EditMode) {
        if (EditMode == HungmanActivity.EditMode.ADDSPACING
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            et.setLetterSpacing(1);
        } else if (EditMode == HungmanActivity.EditMode.ADDSPACING) {
            text = addSpacing(text);
        }

        et.setText(text);
    }

    public String addSpacing(String notSpaced) {
        //LetterSpacing doesn't work below API Level 21, so we insert spaces instead.
        StringBuilder wordWithSpacing = new StringBuilder();
        for (int i = 0; i < notSpaced.length(); i++) {
            char current = notSpaced.charAt(i);
            wordWithSpacing.append(current);
            wordWithSpacing.append(' ');
        }

        return wordWithSpacing.toString();

    }

}
