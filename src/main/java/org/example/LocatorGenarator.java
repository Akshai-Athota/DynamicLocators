package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class LocatorGenarator {
    public static String genearte(String html) throws IOException {
        String prompt = "You are an the greatest automation ui tester.For the given page source please provide all the locators in the json format  in the form"
                + "{locatorName : [list of best 5 locators for the element that i can directly use (xpath|id|class|cssSelectors|partial Link test|names|and so on)]}.\n\n"
                +"For example : {userName : [//*[@id='username'],input[name='username'],//input[@id='username']]}"
                + "The given page source is \n\n" + html;
        String apiKey = "****";
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("OpenAI API key not set in environment variable OPENAI_API_KEY");
        }

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-4o-mini"); // or "gpt-4o" or "gpt-4" depending on your access
        JsonArray messages = new JsonArray();

        // Chat format requires role + content
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", prompt);
        messages.add(userMessage);

        requestBody.add("messages", messages);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost("https://api.openai.com/v1/chat/completions");
            post.setHeader("Authorization", "Bearer " + apiKey);
            post.setHeader("Content-Type", "application/json");

            post.setEntity(new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON));

            HttpClientResponseHandler<String> responseHandler = response -> {
                int status = response.getCode();
                if (status >= 200 && status < 300) {
                    return EntityUtils.toString(response.getEntity());
                } else {
                    throw new RuntimeException("Unexpected response status: " + status);
                }
            };

            String responseBody = client.execute(post, responseHandler);
            Files.writeString(Paths.get("output.txt"), responseBody, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            JsonObject responseJson = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray choices = responseJson.getAsJsonArray("choices");
            if (choices != null && choices.size() > 0) {
                JsonObject message = choices.get(0).getAsJsonObject().getAsJsonObject("message");
                return message.get("content").getAsString().trim();
            }
            return null;

        }
    }
}
