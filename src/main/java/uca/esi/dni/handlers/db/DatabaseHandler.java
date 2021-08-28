package uca.esi.dni.handlers.db;


import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import okhttp3.*;
import uca.esi.dni.types.DatabaseResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The type Database handler.
 */
public class DatabaseHandler implements DatabaseHandlerI {
    /**
     * The constant JSON.
     */
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * The Client.
     */
    final OkHttpClient client = new OkHttpClient();

    /**
     * Gets database directory url.
     *
     * @param url the url
     * @return the database directory url
     * @throws IOException          the io exception
     * @throws NullPointerException the null pointer exception
     */
    public static String getDatabaseDirectoryURL(String url) throws IOException, NullPointerException {
        return getDatabaseDirectoryURL(url, "");
    }

    /**
     * Gets database directory url.
     *
     * @param url       the url
     * @param directory the directory
     * @return the database directory url
     * @throws IOException          the io exception
     * @throws NullPointerException the null pointer exception
     */
    public static String getDatabaseDirectoryURL(String url, String directory) throws IOException, NullPointerException {
        String authToken = generateAuthToken();

        return url + directory + ".json?access_token=" + authToken;
    }

    /**
     * Generate auth token string.
     *
     * @return the string
     * @throws IOException          the io exception
     * @throws NullPointerException the null pointer exception
     */
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

    /**
     * Gets data.
     *
     * @param url the url
     * @return the data
     * @throws IOException               the io exception
     * @throws NullPointerException      the null pointer exception
     * @throws DatabaseResponseException the database response exception
     */
    public String getData(String url) throws IOException, NullPointerException, DatabaseResponseException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        List<String> response = sendRequest(request);
        return parseResponse(response);
    }

    /**
     * Put data string.
     *
     * @param url        the url
     * @param jsonString the json string
     * @return the string
     * @throws IOException               the io exception
     * @throws NullPointerException      the null pointer exception
     * @throws DatabaseResponseException the database response exception
     */
    public String putData(String url, String jsonString) throws IOException, NullPointerException, DatabaseResponseException {
        RequestBody body = RequestBody.create(jsonString, JSON);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        List<String> response = sendRequest(request);
        return parseResponse(response);
    }

    /**
     * Update data string.
     *
     * @param url        the url
     * @param jsonString the json string
     * @return the string
     * @throws IOException               the io exception
     * @throws NullPointerException      the null pointer exception
     * @throws DatabaseResponseException the database response exception
     */
    public String updateData(String url, String jsonString) throws IOException, NullPointerException, DatabaseResponseException {
        RequestBody body = RequestBody.create(jsonString, JSON);
        Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .build();
        List<String> response = sendRequest(request);
        return parseResponse(response);
    }

    /**
     * Delete data string.
     *
     * @param url the url
     * @return the string
     * @throws IOException               the io exception
     * @throws NullPointerException      the null pointer exception
     * @throws DatabaseResponseException the database response exception
     */
    public String deleteData(String url) throws IOException, NullPointerException, DatabaseResponseException {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        List<String> response = sendRequest(request);
        return parseResponse(response);
    }

    /**
     * Send request list.
     *
     * @param request the request
     * @return the list
     * @throws IOException the io exception
     */
    private List<String> sendRequest(Request request) throws IOException {
        List<String> responseStrings = new ArrayList<>();
        try (Response response = client.newCall(request).execute()) {
            responseStrings.add(Integer.toString(response.code()));
            responseStrings.add(Objects.requireNonNull(response.body()).string());
        }
        return responseStrings;
    }

    /**
     * Parse response string.
     *
     * @param response the response
     * @return the string
     * @throws DatabaseResponseException the database response exception
     */
    private String parseResponse(List<String> response) throws DatabaseResponseException {
        if (response.get(0).equals("200")) {
            return response.get(1);
        } else {
            throw new DatabaseResponseException("Failed request: " + response.get(0));
        }
    }
}
