package org.example;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;

import static HAL.Util.PWD;

public class LoggerFactory {

    static String generateFileNameFromDateTime() {

        java.util.Date now = new java.util.Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return dateFormat.format(now) + "_" + RandomStringUtils.randomAlphanumeric(4);
    }

    public static Logger createLogger(JSONObject jsonObject, String identifier) {


        boolean toFile = (boolean) jsonObject.getOrDefault("toFile", false);

        if (jsonObject.containsKey("filename") || toFile) {

            String path = PWD() + "/output/" + identifier + "/";

            String filename = (jsonObject.containsKey("filename")) ?
                (String) jsonObject.get("filename") :
                generateFileNameFromDateTime();

            filename += ".csv";

            return new Logger(path, filename,
                jsonObject.containsKey("logInterval") ? (double) jsonObject.get("logInterval") : null);
        }

        return new Logger(jsonObject.containsKey("logInterval") ? (double) jsonObject.get("logInterval") : null);
    }
}
