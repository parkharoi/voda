package org.delivery.voda.domain.user.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.delivery.voda.domain.user.dto.request.LoginRequest;
import org.delivery.voda.domain.user.dto.request.SignupRequest;
import org.delivery.voda.domain.user.dto.respose.TokenResponse;
import org.delivery.voda.domain.user.entity.User;
import org.delivery.voda.domain.user.enums.SocialType;
import org.delivery.voda.domain.user.enums.UserRole;
import org.delivery.voda.domain.user.repository.UserRepository;
import org.delivery.voda.security.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  @Transactional
  public Long signup(SignupRequest request) {
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

  public TokenResponse login(LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().getKey());

    return new TokenResponse(accessToken, "Bearer");
  }

}
