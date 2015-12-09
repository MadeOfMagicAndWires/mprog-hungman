package nl.mprog.hungman.model;

import java.util.LinkedList;
import java.util.Map;

/**
 * History class
 * Used to read and write Highscores from a file.
 */
public class History {
    LinkedList<Map> highscores;

    public History(){
        highscores = new LinkedList<Map>();
    }

    /**
     * Fetches the highscores from different playsessions
     */
    static public LinkedList<Map> readHighscores(){return null;}

    /**
     * Checks if a score is a new highscore
     * @param newscore score to check against the current highscores
     * @return true if score is a new highscore, false if not.
     */
    public boolean checkHighscores(int newscore){
        return false;
    }

    /**
     * Writes highscores back to file.
     * @return true if successful
     */
    public boolean writeHighscores() {
        return false;
    }


}
