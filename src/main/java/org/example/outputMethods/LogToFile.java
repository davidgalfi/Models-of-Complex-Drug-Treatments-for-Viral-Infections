package org.example.outputMethods;

import HAL.Tools.FileIO;

import java.io.File;

public class LogToFile extends Log {

    /**
     * The FileIO object for writing data to a file during simulation.
     */
    HAL.Tools.FileIO outputFile;

    final String filenameAndPath;

    public LogToFile(String path, String filename, Double logInterval) {

        super(logInterval);

        this.filenameAndPath = path + filename;

        new File(path).mkdirs();
        outputFile = new FileIO(filenameAndPath, "w");
    }

    @Override
    public void logString(String str) {

        outputFile.Write(str);
    }

    @Override
    public void close() {

        outputFile.Close();
    }
}
