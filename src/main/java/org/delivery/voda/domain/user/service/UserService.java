package org.delivery.voda.domain.user.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.delivery.voda.domain.user.dto.request.SignupRequest;
import org.delivery.voda.domain.user.entity.User;
import org.delivery.voda.domain.user.enums.SocialType;
import org.delivery.voda.domain.user.enums.UserRole;
import org.delivery.voda.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public Long Signup(SignupRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
    }

    //비밀번호 암호화
    String encodedPassword = passwordEncoder.encode(request.getPassword());

    User user = User.builder()
        .email(request.getEmail())
        .password(encodedPassword)
        .nickname(request.getNickname())
        .birthDate(LocalDate.parse(request.getBirthDate()))
        .role(UserRole.USER)
        .socialType(SocialType.VODA)
        .build();

    User saveUser = userRepository.save(user);

    return saveUser.getId();

  }

}
