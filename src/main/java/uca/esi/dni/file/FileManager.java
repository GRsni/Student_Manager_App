package uca.esi.dni.file;

import processing.core.PApplet;
import processing.data.JSONObject;
import uca.esi.dni.DniParser;

import java.io.File;
import java.util.ArrayList;

public class FileManager {
    public DniParser parent;
    private File text, json;


    public FileManager(DniParser parent) {
        this.parent = parent;
    }

    public void createNewJSONFromText() {
        JSONObject fileObject = new JSONObject();
        JSONObject users = new JSONObject();
        JSONObject ids = new JSONObject();

        ArrayList<String> validLines = loadLinesFromTextFile();

        if (validLines == null) {
            return;
        }

        for (String dni : validLines) {
            addUserToJSON(users, ids, dni);
        }

        // Se añade el usuario anonimo
        addUserToJSON(users, ids, "u99999999");

        fileObject.setJSONObject("Users", users);
        fileObject.setJSONObject("Ids", ids);

        saveJSONFile(fileObject, "data/databaseFile" + System.currentTimeMillis() + ".json");
        //parent.addNewWarning("Generado nuevo archivo de datos.", 100);
    }

    public void appendNewStudentsToJSONFile() {
        JSONObject fileObject = loadJSONObjectsFromFile();
        if (fileObject == null) {
            //parent.addNewWarning("Error cargando datos de archivo JSON", 100);
            return;
        }

        JSONObject users = fileObject.getJSONObject("Users");
        JSONObject ids = fileObject.getJSONObject("Ids");

        ArrayList<String> validLines = loadLinesFromTextFile();
        if (validLines == null) {
            //parent.addNewWarning("Error cargando los datos de texto", 100);
            return;
        }

        for (String dni : validLines) {
            if (fileObject.getJSONObject("Users").isNull(dni)) {
                addUserToJSON(users, ids, dni);
            }
        }
        saveJSONFile(fileObject, "data/databaseFile" + System.currentTimeMillis() + ".json");
        // parent.addNewWarning("Añadidos nuevos alumnos al archivo.", 100);

    }

    private ArrayList<String> loadLinesFromTextFile() {
        ArrayList<String> validLines;
        try {
            validLines = UtilParser.getCorrectLines(text);
        } catch (NullPointerException e) {
            System.out.println(e.toString());
            return null;
        }
        return validLines;
    }

    private void addUserToJSON(JSONObject users, JSONObject ids, String uId) {
        JSONObject user = new JSONObject();
        user.setString("uId", uId);
        user.setInt("numP", 0);

        users.setJSONObject(uId, user);
        ids.setString(createRandomIndex(), uId);
    }

    private String createRandomIndex() {
        return Long.toString(System.nanoTime()) + (int) (parent.random(1000000));
    }

    private void saveJSONFile(JSONObject object, String filename) {
        parent.saveJSONObject(object, filename);
        System.out.println("Database file generated");
    }


    private static String getDateString(String expanded) {
        return expanded.replace(" ", "-").substring(0, 19);
    }

    private JSONObject loadJSONObjectsFromFile() {
        JSONObject fileObject;
        try {
            fileObject = PApplet.loadJSONObject(json);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        return fileObject;
    }

}
