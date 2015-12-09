package nl.mprog.hungman.manager;

//System imports
import android.util.TimingLogger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

//Import Custom Handler
import nl.mprog.hungman.handler.XmlStringArrayHandler;

/**
 * XmlStringArrayParser class
 * Makes parsing an xml string-array file a one-method call.
 *
 * @see nl.mprog.hungman.handler.XmlStringArrayHandler
 */
public class XmlStringArrayParser {
    /**
     * Parse xml file for <item></item> elements.
     * @param is InputStream of xml file
     * @param maxWordLength maximum string length of items to add
     * @return string-array as ArrayList.
     */
    public static ArrayList<String> parse(InputStream is, int maxWordLength) {
        //Create empty wordlist
        ArrayList<String> wordList = null;

        try{
            //initiate SAX Parser and content handler
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            XmlStringArrayHandler saxHandler;

            //figure out which constructor to use.
            if( maxWordLength == 0) {
                saxHandler = new XmlStringArrayHandler();
            } else { saxHandler = new XmlStringArrayHandler(maxWordLength); }

            //join SAX and Handler
            xmlReader.setContentHandler(saxHandler);
            xmlReader.setErrorHandler(saxHandler);

            //Parse the InputStream, time how long it takes.
            TimingLogger timers = new TimingLogger("Hungman", "readWordList");
            xmlReader.parse(new InputSource(is));
            timers.addSplit("parsing xml");
            timers.dumpToLog();

            //fetch resulting string array.
            wordList = saxHandler.getStringArray();

        } catch (SAXException | ParserConfigurationException | IOException e ) {
            //print any errors that may occur.
            e.printStackTrace();
        }
        //return result.
        return wordList;
    }


}
