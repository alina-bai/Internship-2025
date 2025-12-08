package newusefy.com.internship.repository;

import newusefy.com.internship.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // Все сообщения одной сессии, по времени (от старых к новым)
    List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(Long sessionId);
}
