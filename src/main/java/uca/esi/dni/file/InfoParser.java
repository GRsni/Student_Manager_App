package uca.esi.dni.file;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import processing.core.PApplet;

class InfoParser {
    private static final Pattern idPattern = Pattern.compile("u[a-zA-Z0-9]{8}");

    public static ArrayList<String> getCorrectLines(File text) {
        ArrayList<String> goodLines = new ArrayList<>();

        String[] lines = getLinesFromText(text);
        for (String line : lines) {
            String possibleId = findValidIDInLine(line);
            if (!possibleId.equals("")) {
                goodLines.add(possibleId);
            }
        }

        return goodLines;
    }

    public static String[] getLinesFromText(File text) {
        return PApplet.loadStrings(text);
    }

    private static String findValidIDInLine(String line) {
        return extractId(line.toLowerCase());
    }

    public static String extractId(String line) {

        Matcher m = idPattern.matcher(line);
        String match = "";

        // if we find a match, get the group
        if (m.find()) {
            // we're only looking for one group, so get it
            // and print it out for verification
            match = m.group(0);
        }
        return match;
    }

}