package newusefy.com.internship.repository;

import newusefy.com.internship.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    // Все сессии конкретного пользователя, от новых к старым
    List<ChatSession> findByUserIdOrderByUpdatedAtDesc(Long userId);
}