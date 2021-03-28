package uca.esi.dni.file;


import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import okhttp3.*;
import processing.data.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DatabaseHandler {
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    final OkHttpClient client = new OkHttpClient();

    public static String getDBReference(String filename) {
        JSONObject object = UtilParser.loadJSONObject(filename);
        return object.getString("databaseURL");
    }

    public static String getDatabaseDirectoryURL(String url) throws IOException, NullPointerException {
        return getDatabaseDirectoryURL(url, "");
    }

    public static String getDatabaseDirectoryURL(String url, String directory) throws IOException, NullPointerException {
        String authToken = generateAuthToken();

        return url + directory + ".json?access_token=" + authToken;
    }

    private static String generateAuthToken() throws IOException, NullPointerException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                Objects.requireNonNull(DatabaseHandler.class.getClassLoader().getResourceAsStream("data/files/manualdetorsion-firebase.json")));
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

    public ArrayList<String> getDataFromDB(String url) throws IOException, NullPointerException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return callAndParseResponse(request);
    }

    public ArrayList<String> putDataToDB(String url, String jsonString) throws IOException, NullPointerException {
        RequestBody body = RequestBody.create(jsonString, JSON);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        return callAndParseResponse(request);
    }

    public ArrayList<String> updateData(String url, String jsonString) throws IOException, NullPointerException {
        RequestBody body = RequestBody.create(jsonString, JSON);
        Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .build();
        return callAndParseResponse(request);
    }

    public ArrayList<String> emptyDB(String url) throws IOException, NullPointerException {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        return callAndParseResponse(request);
    }

    private ArrayList<String> callAndParseResponse(Request request) throws IOException {
        ArrayList<String> responseStrings = new ArrayList<>();
        try (Response response = client.newCall(request).execute()) {
            responseStrings.add(Integer.toString(response.code()));
            responseStrings.add(response.body().string());
        }
        return responseStrings;
    }

}
