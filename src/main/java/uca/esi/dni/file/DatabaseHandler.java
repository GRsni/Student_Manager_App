package uca.esi.dni.file;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class DatabaseHandler {
    OkHttpClient client = new OkHttpClient();

    public String getDBReference(PApplet parent, String filename) {
        JSONObject object = parent.loadJSONObject(filename);
        return object.getString("URL");
    }

    public String generateDatabaseDirectoryURL(String url, String directory) throws IOException {
        String authToken = generateAuthToken();

        return url + directory + ".json?access_token=" + authToken;
    }

    public String getDataFromDB(String url) throws IOException, NullPointerException {
        String responseString = "";
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            responseString = response.body().string();
        }
        return responseString;
    }

    private String generateAuthToken() throws IOException {

        // Load the service account key JSON file
        FileInputStream serviceAccount = new FileInputStream("data/files/manualdetorsion-firebase-adminsdk-mgqwd-f49108aaae.json");

        // Authenticate a Google credential with the service account
        GoogleCredential googleCred = GoogleCredential.fromStream(serviceAccount);

        // Add the required scopes to the Google credential
        GoogleCredential scoped = googleCred.createScoped(
                Arrays.asList(
                        "https://www.googleapis.com/auth/firebase.database",
                        "https://www.googleapis.com/auth/userinfo.email"
                )
        );

        // Use the Google credential to generate an access token
        scoped.refreshToken();

        // See the "Using the access token" section below for information
        // on how to use the access token to send authenticated requests to the
        // Realtime Database REST API.
        return scoped.getAccessToken();
    }
}
