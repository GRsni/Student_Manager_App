package uca.esi.dni.file;

import processing.core.PApplet;
import processing.data.JSONObject;
import processing.data.Table;
import processing.data.TableRow;
import uca.esi.dni.DniParser;
import uca.esi.dni.data.Student;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilParser {
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

    public static boolean checkFileExtension(String file, String ext) {
        String fileExtension = DniParser.checkExtension(file);
        return fileExtension.equals(ext);
    }

    private static ArrayList<String> getIdsFromIDList(JSONObject object) {
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> idKeys = getKeysInJSONObject(object);

        for (String key : idKeys) {
            ids.add(object.getString(key));
        }

        return ids;
    }


    public static Map<String, Map<String, Table>> createStudentsDataTables(JSONObject allStudentsList) {
        Map<String, Map<String, Table>> studentTableMap = new HashMap<>();
        ArrayList<String> ids = getKeysInJSONObject(allStudentsList);

        for (String id : ids) {
            Map<String, Table> tableMap = new HashMap<>();
            JSONObject studentData = allStudentsList.getJSONObject(id);

            JSONObject practicas = studentData.getJSONObject("practicas");
            if (practicas != null) {
                ArrayList<String> tiposPracticas = getKeysInJSONObject(practicas);
                for (String tipo : tiposPracticas) {
                    JSONObject practica = practicas.getJSONObject(tipo);
                    if (practica != null) {
                        Table dataTable = parseJSONDataIntoTable(practica);
                        tableMap.put(tipo, dataTable);
                    }
                }
            }
            studentTableMap.put(id, tableMap);
        }
        return studentTableMap;
    }

    private static ArrayList<String> getKeysInJSONObject(JSONObject object) {
        ArrayList<String> keys = new ArrayList<>();

        Object[] keysArray = object.keys().toArray();

        for (Object key : keysArray) {
            keys.add((String) key);
        }
        return keys;
    }

    private static Table parseJSONDataIntoTable(JSONObject labsOneType) {
        Table table = new Table();
        ArrayList<String> labsKeys = getKeysInJSONObject(labsOneType);
        ArrayList<String> labDataKeys = getKeysInJSONObject(labsOneType.getJSONObject(labsKeys.get(0)));

        for (String key : labDataKeys) {
            table.addColumn(key);
        }

        for (String entryKey : labsKeys) {
            JSONObject entry = labsOneType.getJSONObject(entryKey);
            TableRow row = table.addRow();
            populateRowFromPracticaObject(entry, row);
        }

        return table;
    }

    private static void populateRowFromPracticaObject(JSONObject practica, TableRow row) {
        for (String k : getKeysInJSONObject(practica)) {
            try {
                Object v = practica.get(k);
                if (v instanceof Integer || v instanceof Long) {
                    long intToUse = ((Number) v).longValue();
                    row.setLong(k, intToUse);
                } else if (v instanceof Boolean) {
                    boolean boolToUse = (Boolean) v;
                    row.setString(k, Boolean.toString(boolToUse));
                } else if (v instanceof Float || v instanceof Double) {
                    double floatToUse = ((Number) v).doubleValue();
                    row.setDouble(k, floatToUse);
                } else if (JSONObject.NULL.equals(v)) {
                    Object nullToUse = null;

                } else {
                    String stringToUse = practica.getString(k);
                    row.setString(k, stringToUse);
                }
            } catch (Exception e2) {
                System.err.println("exception: " + e2);
                e2.printStackTrace();
            }
        }

    }

    public static Set<Student> generateStudentListFromJSONObject(JSONObject data) throws RuntimeException {
        Set<Student> students = new HashSet<>();
        ArrayList<String> ids = getKeysInJSONObject(data);
        for (String id : ids) {
            Student student = generateStudentFromJSONObject(id, data.getJSONObject(id));
            students.add(student);
        }
        return students;
    }

    private static Student generateStudentFromJSONObject(String id, JSONObject jsonStudent) throws RuntimeException {
        return new Student(id, jsonStudent.getString("email"), jsonStudent.getString("hashKey"));
    }

}