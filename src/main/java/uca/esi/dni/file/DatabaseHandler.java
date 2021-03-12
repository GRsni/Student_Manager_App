package uca.esi.dni.file;


import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import okhttp3.*;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseHandler {
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    OkHttpClient client = new OkHttpClient();

    public String getDBReference(PApplet parent, String filename) {
        JSONObject object = parent.loadJSONObject(filename);
        return object.getString("URL");
    }

    public String generateDatabaseDirectoryURL(String url, String directory) throws IOException {
        String authToken = generateAuthToken();

        return url + directory + ".json?access_token=" + authToken;
    }

    public ArrayList<String> getDataFromDB(String url) throws IOException, NullPointerException {
        ArrayList<String> responseStrings = new ArrayList<>();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            responseStrings.add(Integer.toString(response.code()));
            responseStrings.add(response.body().string());
        }
        return responseStrings;
    }

    public ArrayList<String> putDataToDB(String url, String jsonString) throws IOException, NullPointerException {
        ArrayList<String> responseStrings = new ArrayList<>();
        RequestBody body = RequestBody.create(jsonString, JSON);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            responseStrings.add(Integer.toString(response.code()));
            responseStrings.add(response.body().string());
        }
        return responseStrings;
    }

    public ArrayList<String> updateData(String url, String jsonString) throws IOException, NullPointerException {
        ArrayList<String> responseStrings = new ArrayList<>();
        RequestBody body = RequestBody.create(jsonString, JSON);
        Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            responseStrings.add(Integer.toString(response.code()));
            responseStrings.add(response.body().string());
        }
        return responseStrings;
    }

    public ArrayList<String> emptyDB(String url) throws IOException, NullPointerException {
        ArrayList<String> responseStrings = new ArrayList<>();
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            responseStrings.add(Integer.toString(response.code()));
            responseStrings.add(response.body().string());
        }
        return responseStrings;
    }

    private String generateAuthToken() throws IOException {

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("data/files/manualdetorsion-firebase-adminsdk-mgqwd-f49108aaae.json"));
        GoogleCredentials scoped = credentials.createScoped(
                Arrays.asList(
                        "https://www.googleapis.com/auth/firebase.database",
                        "https://www.googleapis.com/auth/userinfo.email"
                )
        );
        scoped.refreshIfExpired();
        AccessToken token = scoped.getAccessToken();

        // See the "Using the access token" section below for information
        // on how to use the access token to send authenticated requests to the
        // Realtime Database REST API.
        return token.getTokenValue();
    }
}
