package org.delivery.voda.domain.chat.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.delivery.voda.domain.chat.entity.Chat;
import org.delivery.voda.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

  List<Chat> findAllByUserAndCreatedAtBetweenOrderByCreatedAtAsc(
      User user, LocalDateTime start, LocalDateTime end);
}
