package org.delivery.voda.domain.auth.repository;

import java.util.Optional;
import org.delivery.voda.domain.auth.entity.RefreshToken;
import org.delivery.voda.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {


}
