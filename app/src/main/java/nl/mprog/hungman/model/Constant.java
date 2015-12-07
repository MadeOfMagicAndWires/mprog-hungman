package nl.mprog.hungman.model;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Constant class
 * Contains all constants used throughout the nl.mprog.hungman package
 *
 * @author Joost Bremmer
 * @since  1.5
 */
public class Constant {
    static public final String HIGHSCOREFILE = "highscores.csv";
    static public final String WORDLISTFILE  = "words.xml";
    static public final String WORDSMALLFILE = "small.xml";
    public static final String WORDLISTNAME = "wordList";
    public static final String GAMESTATENAME = "gameState";


    static public final List<Pair> DEFAULTCFG = Collections.unmodifiableList(
            new ArrayList<Pair>() {{
                add(Pair.create("wordMaxLength", 7));
                add(Pair.create("lives", 10));
                add(Pair.create("evil", true));
            }});


}
