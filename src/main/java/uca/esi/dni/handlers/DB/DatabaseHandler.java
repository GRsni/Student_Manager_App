package uca.esi.dni.handlers.DB;


import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import okhttp3.*;
import uca.esi.dni.types.DatabaseResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DatabaseHandler implements DatabaseHandlerI {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    final OkHttpClient client = new OkHttpClient();

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

    public String getData(String url) throws IOException, NullPointerException, DatabaseResponseException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        List<String> response = sendRequest(request);
        return parseResponse(response);
    }

    public String putData(String url, String jsonString) throws IOException, NullPointerException, DatabaseResponseException {
        RequestBody body = RequestBody.create(jsonString, JSON);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        List<String> response = sendRequest(request);
        return parseResponse(response);
    }

    public String updateData(String url, String jsonString) throws IOException, NullPointerException, DatabaseResponseException {
        RequestBody body = RequestBody.create(jsonString, JSON);
        Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .build();
        List<String> response = sendRequest(request);
        return parseResponse(response);
    }

    public String deleteData(String url) throws IOException, NullPointerException, DatabaseResponseException {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        List<String> response = sendRequest(request);
        return parseResponse(response);
    }

    private List<String> sendRequest(Request request) throws IOException {
        List<String> responseStrings = new ArrayList<>();
        try (Response response = client.newCall(request).execute()) {
            responseStrings.add(Integer.toString(response.code()));
            responseStrings.add(Objects.requireNonNull(response.body()).string());
        }
        return responseStrings;
    }

    private String parseResponse(List<String> response) throws DatabaseResponseException {
        if (response.get(0).equals("200")) {
            return response.get(1);
        } else {
            throw new DatabaseResponseException("Failed request: " + response.get(0));
        }
    }
}
