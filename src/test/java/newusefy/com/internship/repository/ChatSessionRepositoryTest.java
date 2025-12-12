package newusefy.com.internship.repository;

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
 * Тесты уровня репозитория для ChatSessionRepository.
 * Используем @DataJpaTest: поднимается только слой JPA + H2 в памяти.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ChatSessionRepositoryTest {

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Test
    @DisplayName("Сохранение и загрузка ChatSession по id")
    void saveAndFindById() {
        // given
        ChatSession session = ChatSession.builder()
                .title("Test session")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                // user оставляем null, чтобы не зависеть от сущности User
                .build();

        ChatSession saved = chatSessionRepository.save(session);

        // when
        ChatSession found = chatSessionRepository.findById(saved.getId())
                .orElseThrow(() -> new IllegalStateException("Session not found"));

        // then
        assertThat(found.getId()).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Test session");
    }

    @Test
    @DisplayName("findAll возвращает сохранённые сессии")
    void findAllReturnsSavedSessions() {
        // given
        ChatSession s1 = ChatSession.builder()
                .title("S1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ChatSession s2 = ChatSession.builder()
                .title("S2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        chatSessionRepository.save(s1);
        chatSessionRepository.save(s2);

        // when
        List<ChatSession> all = chatSessionRepository.findAll();

        // then
        assertThat(all).hasSize(2);
        assertThat(all)
                .extracting(ChatSession::getTitle)
                .containsExactlyInAnyOrder("S1", "S2");
    }
}

