package org.example.utils;

import org.apache.commons.lang3.RandomStringUtils;

import static HAL.Util.PWD;

public class PathAndFile {

    public static String generateFileNameFromDateTime() {

        java.util.Date now = new java.util.Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return dateFormat.format(now) + "_" + RandomStringUtils.randomAlphanumeric(4);
    }

    public static String generatePathFromIdentifier(String identifier) {
        return PWD() + "/output/" + identifier + "/";
    }
}
