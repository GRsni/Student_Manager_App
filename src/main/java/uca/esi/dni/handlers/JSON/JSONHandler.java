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

/**
 * The type Json handler.
 */
public class JSONHandler implements JSONHandlerI {
    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Instantiates a new Json handler.
     */
    private JSONHandler() {
    }

    /**
     * Load json object json object.
     *
     * @param filepath the filepath
     * @return the json object
     */
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

    /**
     * Parse json object json object.
     *
     * @param text the text
     * @return the json object
     */
    public static JSONObject parseJSONObject(String text) {
        if (text != null && !text.isEmpty() && !text.equals("null")) {
            return JSONObject.parse(text);
        } else {
            return new JSONObject();
        }
    }

    /**
     * Gets json object keys.
     *
     * @param object the object
     * @return the json object keys
     * @throws NullPointerException the null pointer exception
     */
    public static List<String> getJSONObjectKeys(JSONObject object) throws NullPointerException {
        List<String> keys = new ArrayList<>();

        for (Object key : object.keys()) {
            keys.add((String) key);
        }
        return keys;
    }

    /**
     * Generate multi path json string string.
     *
     * @param urlContentsMap the url contents map
     * @return the string
     * @throws NullPointerException the null pointer exception
     */
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

    /**
     * Gets string value in json object.
     *
     * @param jsonObject the json object
     * @param key        the key
     * @return the string value in json object
     */
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
