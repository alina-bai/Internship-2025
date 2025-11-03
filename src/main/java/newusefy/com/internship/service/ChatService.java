package newusefy.com.internship.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public ChatService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String chat(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "{\"reply\": \"Сообщение не может быть пустым.\"}";
        }

        try {
            String sanitizedMessage = message.replace("\\", "\\\\").replace("\"", "\\\"");
            String finalApiUrl = apiUrl + "?key=" + apiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestBody = """
            {
                "contents": [{
                    "parts": [{"text": "%s"}]
                }]
            }
            """.formatted(sanitizedMessage);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response =
                    restTemplate.exchange(finalApiUrl, HttpMethod.POST, entity, String.class);

            // ✅ Парсим JSON-ответ Gemini
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode candidates = root.path("candidates");

            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode textNode = candidates.get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text");

                if (textNode.isTextual()) {
                    return "{\"reply\": \"" + textNode.asText().replace("\"", "\\\"") + "\"}";
                }
            }

            // если структура неожиданная
            return "{\"reply\": \"⚠️ AI ответ не распознан: " + response.getBody() + "\"}";
        } catch (Exception e) {
            System.err.println("Gemini API Error: " + e.getMessage());
            return "{\"reply\": \"Ошибка связи с AI: " + e.getMessage() + "\"}";
        }
    }
}
