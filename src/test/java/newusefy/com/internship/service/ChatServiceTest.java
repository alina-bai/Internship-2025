package newusefy.com.internship.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChatServiceTest {

    private RestTemplate restTemplate;
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        chatService = new ChatService(restTemplate); // ✅ теперь только 1 аргумент
    }


    @Test
    void testChat_SuccessfulResponse() {
        // Мокаем ответ
        ResponseEntity<String> mockResponse =
                new ResponseEntity<>("AI says: Hello!", HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class))
        ).thenReturn(mockResponse);

        String result = chatService.chat("Hi!");
        assertEquals("AI says: Hello!", result);
    }

    @Test
    void testChat_ErrorResponse() {
        ResponseEntity<String> mockResponse =
                new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class)))
                .thenReturn(mockResponse);

        String result = chatService.chat("test");
        assertTrue(result.contains("Error: 400"));
    }

    @Test
    void testChat_ExceptionThrown() {
        when(restTemplate.exchange(anyString(), any(), any(), eq(String.class)))
                .thenThrow(new RuntimeException("Connection timeout"));

        String result = chatService.chat("Hi!");
        assertTrue(result.contains("Failed to contact AI service"));
    }

    @Test
    void testChat_RequestBodyAndHeadersAreCorrect() {
        ResponseEntity<String> mockResponse =
                new ResponseEntity<>("OK", HttpStatus.OK);

        ArgumentCaptor<HttpEntity> captor = ArgumentCaptor.forClass(HttpEntity.class);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), captor.capture(), eq(String.class)))
                .thenReturn(mockResponse);

        chatService.chat("Test message");

        HttpEntity<String> sentRequest = captor.getValue();
        assertTrue(sentRequest.getBody().contains("Test message"));
        assertEquals("Bearer FAKE_KEY",
                sentRequest.getHeaders().getFirst("Authorization"));
    }
}
