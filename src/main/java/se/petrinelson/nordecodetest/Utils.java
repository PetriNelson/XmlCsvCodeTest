package se.petrinelson.nordecodetest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.petrinelson.nordecodetest.constants.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;

public class Utils {
    private static final Logger LOGGER = LogManager.getLogger(Utils.class);
    /**
     * Simple File Reader
     */

    public static String readFile(Path path) {
        String response = "";
        try {
            FileReader fr = new FileReader(path.toString());
            BufferedReader br = new BufferedReader(fr);
            String strLine;
            StringBuffer sb = new StringBuffer();
            while ((strLine = br.readLine()) != null) {
                sb.append(strLine);
            }
            response = sb.toString();
            System.out.println(response);
            fr.close();
            br.close();
        } catch (Exception ex) {
            Utils.err(ex);
        }
        return response;
    }

    public static void err(Exception ex) {
        LOGGER.error(Constants.GENERIC_EXCEPTION + " " + ex);
    }
}
