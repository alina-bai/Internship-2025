package newusefy.com.internship.controller;

import newusefy.com.internship.dto.ChatRequestDto;
import newusefy.com.internship.dto.ChatResponseDto;
import newusefy.com.internship.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ChatController.class)
@Import(ChatControllerTest.MockConfig.class)  // подключаем конфигурацию мока

public class ChatControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ChatService chatService() {
            return Mockito.mock(ChatService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        when(chatService.chat("Hello")).thenReturn("Hi there!");
    }

    @Test
    void testChatEndpointReturnsOk() throws Exception {
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"Hello\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value("Hi there!"));
    }

    @Test
    void testChatEndpointReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}
