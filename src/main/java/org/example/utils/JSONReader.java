package org.example.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JSONReader{

    public static Path getFilePath(String relativePath) throws URISyntaxException {
        return Paths.get(
                ClassLoader.getSystemResource(relativePath).toURI()
        );
    }

    public static JSONObject getJSONObjectFromJSON(String path) throws URISyntaxException {

        Path filePath = getFilePath(path);

        JSONParser parser = new JSONParser();

        JSONObject jsonObject;

        try (FileReader reader = new FileReader(filePath.toFile())) {

            jsonObject = (JSONObject) parser.parse(reader);

        } catch (Exception e) {
            jsonObject = new JSONObject();
            e.printStackTrace();
        }
        return jsonObject;
    }
}
