package uca.esi.dni.handlers;

import org.jetbrains.annotations.NotNull;
import processing.data.JSONObject;
import processing.data.Table;
import processing.data.TableRow;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class JSONHandler {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private JSONHandler(){}

    public static JSONObject loadJSONObject(String filepath) {
        if (filepath != null && Util.checkFileExtension(filepath, "json")) {
            try {
                InputStream inputStream = Util.class.getClassLoader().getResourceAsStream(filepath);
                String fileContent = Util.readFromInputStream(inputStream);
                return parseJSONObject(fileContent);
            } catch (IOException | NullPointerException e) {
                LOGGER.severe("[Error while reading JSON file]: " + e.getMessage());
                return new JSONObject();
            }
        }
        return new JSONObject();
    }

    public static JSONObject parseJSONObject(String text) {
        if (text != null && !text.isEmpty() && !text.equals("null")) {
            return JSONObject.parse(text);
        } else {
            return new JSONObject();
        }
    }

    public static List<String> getJSONObjectKeys(JSONObject object) throws NullPointerException {
        List<String> keys = new ArrayList<>();

        for (Object key : object.keys()) {
            keys.add((String) key);
        }
        return keys;
    }

    public static String generateMultiPathJSONString(@NotNull Map<String, JSONObject> urlContentsMap) throws NullPointerException {
        JSONObject multipath = new JSONObject();
        for (Map.Entry<String, JSONObject> entry : urlContentsMap.entrySet()) {
            List<String> secondLevelKeys = getJSONObjectKeys(urlContentsMap.get(entry.getKey()));
            for (String key : secondLevelKeys) {
                multipath.put(entry.getKey() + "/" + key, getStringValueInJSONObject(entry.getValue(), key));
            }
        }
        return multipath.toString();
    }

    private static Object getStringValueInJSONObject(JSONObject jsonObject, String key) {
        if (jsonObject == null || jsonObject.size() == 0) {
            return null;
        } else if (jsonObject.isNull(key)) {
            return JSONObject.NULL;
        } else {
            return jsonObject.getString(key);
        }
    }

    public static Table parseJSONDataIntoTable(JSONObject jsonObject) {
        Table table = new Table();
        List<String> entries = getJSONObjectKeys(jsonObject);
        List<String> entryKeys = getJSONObjectKeys(jsonObject.getJSONObject(entries.get(0)));

        for (String key : entryKeys) {
            table.addColumn(key);
        }

        for (String entry : entries) {
            JSONObject entryJSONObject = jsonObject.getJSONObject(entry);
            TableRow row = table.addRow();
            populateRowFromJSONObject(entryJSONObject, row);
        }

        return table;
    }

    private static void populateRowFromJSONObject(JSONObject jsonObject, TableRow row) {
        for (String k : getJSONObjectKeys(jsonObject)) {
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
                LOGGER.warning("[Exception while reading JSONObject]: " + e2.getMessage());
            }
        }
    }
}
