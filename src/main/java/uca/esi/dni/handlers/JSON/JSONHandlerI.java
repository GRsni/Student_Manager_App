package uca.esi.dni.handlers.JSON;

import org.jetbrains.annotations.NotNull;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface JSONHandlerI {

    static JSONObject loadJSONObject(String filepath) {
        return new JSONObject();
    }

    static JSONObject parseJSONObject(String text) {
        return new JSONObject();
    }

    static List<String> getJSONObjectKeys(JSONObject object) throws NullPointerException {
        return new ArrayList<>();
    }

    static String generateMultiPathJSONString(@NotNull Map<String, JSONObject> urlContentsMap) throws NullPointerException {
        return "";
    }
}
