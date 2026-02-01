package newusefy.com.internship.repository;

import newusefy.com.internship.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

    // ⭐ старый метод — ОСТАВЛЯЕМ
    List<ChatSession> findByUserIdOrderByUpdatedAtDesc(Long userId);

    // ⭐ Week 13 — ⭐ активный чат пользователя в конкретной секции
    Optional<ChatSession> findByUserIdAndSectionIdAndActiveTrue(
            Long userId,
            Long sectionId
    );

    // ⭐ может пригодиться для админки / дебага
    List<ChatSession> findBySectionId(Long sectionId);
}

