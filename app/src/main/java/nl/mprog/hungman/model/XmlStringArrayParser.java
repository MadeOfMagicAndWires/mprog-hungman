package nl.mprog.hungman.model;

import android.util.Log;
import android.util.TimingLogger;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import nl.mprog.hungman.handler.XmlStringArrayHandler;

/**
 * XmlStringArrayParser class
 * Makes parsing an xml string-array file a one-method call.
 */
public class XmlStringArrayParser {
    /**
     * Parse xml file for <item></item> elements.
     * @param is InputStream of xml file
     * @param maxWordLength maximum string length of items to add
     * @return string-array as ArrayList.
     */
    public static ArrayList<String> parse(InputStream is, int maxWordLength) {
        ArrayList<String> wordList = null;

        try{
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            XmlStringArrayHandler saxHandler;
            if( maxWordLength == 0) {
                saxHandler = new XmlStringArrayHandler();
            } else { saxHandler = new XmlStringArrayHandler(maxWordLength); }

            xmlReader.setContentHandler(saxHandler);
            xmlReader.setErrorHandler(saxHandler);

            TimingLogger timers = new TimingLogger("Hungman", "readWordList");
            xmlReader.parse(new InputSource(is));
            timers.addSplit("parsing xml");
            timers.dumpToLog();

            wordList = saxHandler.getStringArray();

        } catch (SAXException | ParserConfigurationException | IOException e ) {
            e.printStackTrace();
        }
        return wordList;
    }


}
