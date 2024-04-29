package org.example;


import org.example.outputMethods.Log;
import org.example.outputMethods.LogToFile;
import org.example.utils.PathAndFile;
import org.json.simple.JSONObject;

public class LoggerFactory {

    public static Logger createLogger(JSONObject jsonObject, String identifier) {


        boolean toFile = (boolean) jsonObject.getOrDefault("toFile", false);

        if (jsonObject.containsKey("filename") || toFile) {

            String path = PathAndFile.generatePathFromIdentifier(identifier);

            String filename = (String) jsonObject.getOrDefault("filename", PathAndFile.generateFileNameFromDateTime());

            filename += ".csv";

            return new Logger(new LogToFile(path, filename,
                jsonObject.containsKey("logInterval") ? (double) jsonObject.get("logInterval") : null));
        }

        return new Logger(new Log(jsonObject.containsKey("logInterval") ? (double) jsonObject.get("logInterval") : null));
    }
}
