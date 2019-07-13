package ru.allformine.afmbans.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JsonRequest {
    private String responseString;

    public JsonRequest(URL url, JsonObject json) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);

        byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);

        OutputStream os = connection.getOutputStream();
        os.write(input, 0, input.length);

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }

        this.responseString = response.toString();
    }

    public JsonObject getResponseJson() {
        return new Gson().fromJson(this.responseString.toString(), JsonObject.class);
    }

    public String getResponseString() {
        return this.responseString;
    }
}
