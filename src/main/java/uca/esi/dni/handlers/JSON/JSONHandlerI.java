package uca.esi.dni.handlers.JSON;

import org.jetbrains.annotations.NotNull;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The interface Json handler i.
 */
public interface JSONHandlerI {

    /**
     * Load json object json object.
     *
     * @param filepath the filepath
     * @return the json object
     */
    static JSONObject loadJSONObject(String filepath) {
        return new JSONObject();
    }

    /**
     * Parse json object json object.
     *
     * @param text the text
     * @return the json object
     */
    static JSONObject parseJSONObject(String text) {
        return new JSONObject();
    }

    /**
     * Gets json object keys.
     *
     * @param object the object
     * @return the json object keys
     * @throws NullPointerException the null pointer exception
     */
    static List<String> getJSONObjectKeys(JSONObject object) throws NullPointerException {
        return new ArrayList<>();
    }

    /**
     * Generate multi path json string string.
     *
     * @param urlContentsMap the url contents map
     * @return the string
     * @throws NullPointerException the null pointer exception
     */
    static String generateMultiPathJSONString(@NotNull Map<String, JSONObject> urlContentsMap) throws NullPointerException {
        return "";
    }
}
