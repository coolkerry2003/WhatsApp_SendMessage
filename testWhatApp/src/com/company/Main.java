package com.company;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        sendMessagebyWhatsApp("<targetPhoneNumber>>", "This is an example of a text message");
    }

    public static void sendMessagebyWhatsApp(String targetNumber, String contexts) {
        try {
            String phoneNumber = "<SourcePhoneNumber>";
            String bearerToken = "<Token>";
            URL url = new URL("https://graph.facebook.com/v17.0/" + phoneNumber + "/messages");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //set the http property refer to line notify document.
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Authorization", "Bearer " + bearerToken);
            connection.addRequestProperty("Content-Type", "application/json");

            connection.setConnectTimeout(15 * 1000);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setReadTimeout(15 * 1000);
            connection.setDoInput(true);

            OutputStream os = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8);

            //template模板
//            writer.write("{ \"messaging_product\": \"whatsapp\", " +
//                    "\"to\": \"" + targetNumber + "\", " +
//                    "\"recipient_type\": \"individual\", "+
//                    "\"type\": \"template\", " +
//                    "\"template\": { \"name\": \"hello_world\", \"language\": { \"code\": \"en_US\" } } }");

            //注意，需先執行上述的template，再回覆訊息過去，才會收到訊息
            writer.write("{ \"messaging_product\": \"whatsapp\", " +
                    "\"to\": \"" + targetNumber + "\", " +
                    "\"recipient_type\": \"individual\", " +
                    "\"type\": \"text\", " +
                    "\"text\": { \"body\": \"" + contexts + "\" } }");

            writer.flush();
            writer.close();
            os.close();

            BufferedReader br = null;
            if (100 <= connection.getResponseCode() && connection.getResponseCode() <= 399) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String responseBody = br.lines().collect(Collectors.joining());
            System.out.println(responseBody);
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}