package uca.esi.dni.file;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import processing.data.JSONObject;
import processing.data.Table;
import processing.data.TableRow;
import uca.esi.dni.DniParser;
import uca.esi.dni.data.Student;

import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilParser {
    private static final Pattern idPattern = Pattern.compile("u[a-zA-Z0-9]{8}");
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static String extractId(String line) {

        Matcher m = idPattern.matcher(line);
        String match = "";

        // if we find a match, get the group
        if (m.find()) {
            match = m.group(0);
        }
        return match;
    }

    public static boolean checkFileExtension(String file, String ext) {
        String fileExtension = DniParser.checkExtension(file);
        if (fileExtension == null) {
            return false;
        } else {
            return fileExtension.equals(ext);
        }
    }

    public static String getSHA256HashedString(String plain) {
        return Hashing.sha256().hashString(plain, Charsets.UTF_8).toString();
    }

    public static JSONObject parseJSONObject(String text) {
        if (text != null && !text.isEmpty() && !text.equals("null")) {
            return JSONObject.parse(text);
        } else {
            return new JSONObject();
        }
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

    private static Table parseJSONDataIntoTable(JSONObject jsonObject) {
        Table table = new Table();
        ArrayList<String> entries = getKeysInJSONObject(jsonObject);
        ArrayList<String> entryKeys = getKeysInJSONObject(jsonObject.getJSONObject(entries.get(0)));

        for (String key : entryKeys) {
            table.addColumn(key);
        }

        for (String entry : entries) {
            JSONObject entryJSONObject = jsonObject.getJSONObject(entry);
            TableRow row = table.addRow();
            populateRowFromPracticaObject(entryJSONObject, row);
        }

        return table;
    }

    private static void populateRowFromPracticaObject(JSONObject jsonObject, TableRow row) {
        for (String k : getKeysInJSONObject(jsonObject)) {
            try {
                Object v = jsonObject.get(k);
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
                    row.setString(k, "null");
                } else {
                    String stringToUse = jsonObject.getString(k);
                    row.setString(k, stringToUse);
                }
            } catch (Exception e2) {
                System.err.println("[Exception while reading JSONObject]: " + e2.getMessage());
                LOGGER.warning("[Exception while reading JSONObject]: " + e2.getMessage());
            }
        }

    }

    public static String generateMultiPathJSONString(Map<String, JSONObject> urlContentsMap) {
        JSONObject multipath = new JSONObject();
        for (String url : urlContentsMap.keySet()) {
            ArrayList<String> secondLevelKeys = getKeysInJSONObject(urlContentsMap.get(url));
            for (String key : secondLevelKeys) {
                multipath.put(url + "/" + key, urlContentsMap.get(url).get(key));
            }
        }
        return multipath.toString();
    }

    public static JSONObject getStudentAttributeJSONObject(Set<Student> students, String attribute) {
        JSONObject jsonObject = new JSONObject();
        for (Student student : students) {
            if (attribute != null) {
                jsonObject.setString(student.getID(), student.getAttributeFromStudent(attribute));
            } else {
                jsonObject.put(student.getID(), JSONObject.NULL);
            }
        }
        return jsonObject;
    }

    public static Set<Student> generateStudentListFromJSONObject(JSONObject hashKeys, JSONObject emails) throws RuntimeException {
        Set<Student> students = new HashSet<>();
        ArrayList<String> ids = getKeysInJSONObject(emails);
        for (String id : ids) {
            Student student = new Student(id, emails.getString(id), hashKeys.getString(id));
            students.add(student);
        }
        return students;
    }

    public static Set<String> studentSetToStringSet(Set<Student> students) {
        Set<String> stringSet = new HashSet<>();
        for (Student s : students) {
            stringSet.add(s.getID());
        }
        return stringSet;
    }

    public static Set<Student> getUniqueStudentSet(Set<Student> set1, Set<Student> set2) {
        Set<Student> unique = new HashSet<>();
        for (Student s : set1) {
            boolean found = false;
            for (Student s2 : set2) {
                if (s.getID().equals(s2.getID())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                unique.add(s);
            }
        }
        return unique;
    }

    public static Set<Student> getCoincidentStudentSet(Set<Student> set1, Set<Student> set2) {
        Set<Student> coincident = new HashSet<>();
        for (Student s : set1) {
            for (Student s2 : set2) {
                if (s.getID().equals(s2.getID())) {
                    coincident.add(s2);
                    break;
                }
            }
        }
        return coincident;
    }
}