package newusefy.com.internship.repository;

import newusefy.com.internship.entity.ChatMessage;
import newusefy.com.internship.entity.ChatSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты для ChatMessageRepository.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ChatMessageRepositoryTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Test
    @DisplayName("Сохранение сообщения, связанного с сессией")
    void saveMessageWithSession() {
        // given: сначала создаём и сохраняем сессию
        ChatSession session = ChatSession.builder()
                .title("Repo test session")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        ChatSession savedSession = chatSessionRepository.save(session);

        ChatMessage msg = ChatMessage.builder()
                .session(savedSession)
                .role("user")
                .content("Hello from test")
                .createdAt(LocalDateTime.now())
                .build();

        // when
        ChatMessage savedMsg = chatMessageRepository.save(msg);

        // then
        ChatMessage found = chatMessageRepository.findById(savedMsg.getId())
                .orElseThrow(() -> new IllegalStateException("Message not found"));

        assertThat(found.getSession().getId()).isEqualTo(savedSession.getId());
        assertThat(found.getRole()).isEqualTo("user");
        assertThat(found.getContent()).isEqualTo("Hello from test");
    }

    /**
     * Если в твоём ChatMessageRepository есть метод вроде
     * List<ChatMessage> findBySessionOrderByCreatedAtAsc(ChatSession session),
     * то можно протестировать и его.
     */
    @Test
    @DisplayName("Загрузка сообщений по сессии (пример)")
    void loadMessagesBySession() {
        ChatSession session = ChatSession.builder()
                .title("Session with messages")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        ChatSession savedSession = chatSessionRepository.save(session);

        ChatMessage m1 = ChatMessage.builder()
                .session(savedSession)
                .role("user")
                .content("Hi")
                .createdAt(LocalDateTime.now().minusMinutes(1))
                .build();

        ChatMessage m2 = ChatMessage.builder()
                .session(savedSession)
                .role("ai")
                .content("Hello!")
                .createdAt(LocalDateTime.now())
                .build();

        chatMessageRepository.save(m1);
        chatMessageRepository.save(m2);

        // Если у тебя есть КАСТОМНЫЙ метод - раскомментируй и поправь имя:
        // List<ChatMessage> messages =
        //        chatMessageRepository.findBySessionOrderByCreatedAtAsc(savedSession);

        List<ChatMessage> messages = chatMessageRepository.findAll(); // <- заглушка, если метода нет

        assertThat(messages).isNotEmpty();
    }
}
