package org.delivery.voda.domain.user.repository;

import java.util.Optional;
import org.delivery.voda.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  // 이메일 중복 확인
  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

}
