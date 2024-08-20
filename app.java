import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class OpenAIRequestExample {

    private static final String API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static void main(String[] args) {
        try {
            if (API_KEY == null || API_KEY.isEmpty()) {
                throw new IllegalStateException("API key not found. Please set the OPENAI_API_KEY environment variable.");
            }
            
            JSONObject requestPayload = new JSONObject();
            requestPayload.put("model", "gpt-3.5-turbo");

            requestPayload.put("messages", new JSONObject[]{
                    new JSONObject().put("role", "system").put("content", "你是一個熱愛幫忙的助手."),
                    new JSONObject().put("role", "user").put("content", "法國的首都在哪裡?")
            });

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(requestPayload.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonResponse = new JSONObject(response.body());
            String reply = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            System.out.println("GPT-3.5 Response: " + reply);

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
