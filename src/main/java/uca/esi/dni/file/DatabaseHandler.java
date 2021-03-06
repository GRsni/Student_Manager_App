package uca.esi.dni.file;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.IOException;

public class DatabaseHandler {
    OkHttpClient client = new OkHttpClient();

    public static String getDBReference(PApplet parent, String filename) {
        JSONObject object = parent.loadJSONObject(filename);
        return object.getString("URL");
    }

    public static String generateDatabaseDirectoryURL(String url, String directory) {
        return url + directory + ".json";
    }

    public static JSONObject getJSONDataFromDatabase() {
        return new JSONObject();
    }

    public String connectToDatabase(String url) throws IOException, NullPointerException {
        String responseString = "";
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            responseString = response.body().string();
        }
        return responseString;
    }

}
