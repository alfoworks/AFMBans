package ru.allformine.afmbans.net;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import ru.allformine.afmbans.AFMBans;
import ru.allformine.afmbans.PluginStatics;
import ru.allformine.afmbans.PluginUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JsonRequest {
    private String responseString;
    private int responseCode;
    public JsonRequest(URL url, JsonElement json) throws Exception {
        byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.setRequestProperty("Content-Type", "application/json; utf-8");

        connection.connect();

        OutputStream os = connection.getOutputStream();
        os.write(input, 0, input.length);

        this.responseCode = connection.getResponseCode();

        if (this.responseCode >= 200 && this.responseCode < 400) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            this.responseString = response.toString();
        } else if (connection.getErrorStream() != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append("\n").append(line);
            }
            this.responseString = result.toString();
        }
    }

    public JsonObject getResponseJson() {
        PluginUtils.debug("Receiving JSON: " + this.responseString);

        JsonObject json;

        try {
            json = new Gson().fromJson(this.responseString, JsonObject.class);
        } catch (JsonSyntaxException e) {
            AFMBans.logger.error("Error parsing JSON\n" + this.responseString);
            return null;
        }

        return json;
    }

    public String getResponseString() {
        return this.responseString;
    }

    public int getResponseCode() {
        return this.responseCode;
    }
}
