package org.example.locatorUtills;

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


public class LocatorGenarator {
    public static String genearte(String html) throws IOException {
        String prompt = "You are an the greatest automation ui tester.For the given page source please provide all the locators of the page that can be clickable,interactable,headings,links..etc in the json format  in the form"
                + "{locatorName : [list of best 5 locators for the web elements that can used to  to locate elements on dom]}.\n\n" +"for input add Input at the end of the locator name and for button add Button and for link add Link and so on"
                +"For example : {usernameInput : [xpath=[//*[@id='username'],css=input[name='username'],xpath=//input[@id='username']]}" + "Note : content should be only json no extra information needed\n"
                + "The given page source is \n\n" + html;
        String apiKey = "****";
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("OpenAI API key not set in environment variable OPENAI_API_KEY");
        }

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-4o"); // or "gpt-4o" or "gpt-4" depending on your access
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

            JsonObject responseJson = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray choices = responseJson.getAsJsonArray("choices");
            if (choices != null && choices.size() > 0) {
                JsonObject message = choices.get(0).getAsJsonObject().getAsJsonObject("message");
                String locators = message.get("content").getAsString().trim();
                return locators.substring(7).split("}")[0]+"}";
            }
            return null;

        }
    }
}
