package newusefy.com.internship.service;

import newusefy.com.internship.repository.ChatMessageRepository;
import newusefy.com.internship.repository.ChatSessionRepository;
import newusefy.com.internship.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Упрощённые тесты для ChatService.
 * Здесь НЕТ никакой логики с User / UserRepository,
 * чтобы не зависеть от структуры твоего User.
 *
 * Мы тестируем только метод generateAnswer(...),
 * который ходит в Gemini и парсит ответ.
 */
class ChatServiceTest {

    private RestTemplate restTemplate;
    private ChatSessionRepository chatSessionRepository;
    private ChatMessageRepository chatMessageRepository;
    private UserRepository userRepository;

    private ChatService chatService;

    @BeforeEach
    void setUp() {
        // Моки зависимостей
        restTemplate = mock(RestTemplate.class);
        chatSessionRepository = mock(ChatSessionRepository.class);
        chatMessageRepository = mock(ChatMessageRepository.class);
        userRepository = mock(UserRepository.class);

        // IMPORTANT: конструктор должен совпадать с тем,
        // который сейчас в твоём ChatService
        chatService = new ChatService(
                restTemplate,
                chatSessionRepository,
                chatMessageRepository,
                userRepository
        );
    }

    @Test
    void generateAnswer_shouldReturnValidationMessage_whenPromptIsEmpty() {
        // given
        String emptyPrompt = "   ";

        // when
        String result = chatService.generateAnswer(emptyPrompt);

        // then
        assertThat(result).isEqualTo("Сообщение не может быть пустым.");
    }

    @Test
    void generateAnswer_shouldParseTextFromGeminiResponse() {
        // given: подделываем JSON-ответ от Gemini
        String geminiJson = """
            {
              "candidates": [
                {
                  "content": {
                    "parts": [
                      { "text": "Hello from Gemini" }
                    ]
                  }
                }
              ]
            }
            """;

        ResponseEntity<String> mockResponse =
                new ResponseEntity<>(geminiJson, HttpStatus.OK);

        // когда ChatService вызовет restTemplate.exchange(...),
        // вернуть поддельный ответ
        when(restTemplate.exchange(
                anyString(),                  // URL
                any(HttpMethod.class),        // метод
                any(HttpEntity.class),        // тело + заголовки
                (Class<String>) any()         // тип ответа
        )).thenReturn(mockResponse);

        // when
        String result = chatService.generateAnswer("Привет!");

        // then
        assertThat(result).isEqualTo("Hello from Gemini");
    }
}
