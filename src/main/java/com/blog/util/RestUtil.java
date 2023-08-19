package com.blog.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class RestUtil {

    public static String sendGetRest(String apiUrl, String beerToken) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + beerToken);
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();

            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            return response.toString();
        } else {
            throw new IOException("API request failed with response code: " + responseCode);
        }
    }

    public static String sendPostRest(String apiUrl, String beerToken, Object body) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(apiUrl);

        httpPost.setHeader("Authorization", "Bearer " + (Objects.isNull(beerToken) ? "" :  beerToken));
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Access-Control-Allow-Headers", "Content-Type");
        httpPost.setHeader("Access-Control-Allow-Methods", "*");
        httpPost.setHeader("Access-Control-Allow-Origin", "*");
        httpPost.setHeader("Access-Control-Allow-Credentials", "true");

        String jsonBody = UtilFunction.convertObjectToString(body);

        // Đặt nội dung (body) của yêu cầu POST
        httpPost.setEntity(new StringEntity(jsonBody, "UTF-8"));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            EntityUtils.consume(entity); // Đảm bảo giải phóng tài nguyên
            return responseBody;
        }
    }

}
