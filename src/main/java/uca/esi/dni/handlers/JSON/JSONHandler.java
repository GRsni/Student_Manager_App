package uca.esi.dni.handlers.JSON;

import org.jetbrains.annotations.NotNull;
import processing.data.JSONObject;
import uca.esi.dni.handlers.Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class JSONHandler implements JSONHandlerI {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private JSONHandler() {
    }

    public static JSONObject loadJSONObject(String filepath) {
        JSONObject object = new JSONObject();
        if (filepath != null && Util.checkFileExtension(filepath, "json")) {
            try {
                InputStream inputStream = Util.class.getClassLoader().getResourceAsStream(filepath);
                String fileContent = Util.readFromInputStream(inputStream);
                object = parseJSONObject(fileContent);
            } catch (IOException | NullPointerException e) {
                LOGGER.severe("[Error while reading JSON file]: " + e.getMessage());
            }
        }
        return object;
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
}
