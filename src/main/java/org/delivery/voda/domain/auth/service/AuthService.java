package org.delivery.voda.domain.auth.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.delivery.voda.domain.auth.entity.RefreshToken;
import org.delivery.voda.domain.auth.repository.RefreshTokenRepository;
import org.delivery.voda.domain.auth.dto.request.LoginRequest;
import org.delivery.voda.domain.auth.dto.request.SignupRequest;
import org.delivery.voda.domain.auth.dto.respose.TokenResponse;
import org.delivery.voda.domain.user.entity.User;
import org.delivery.voda.domain.user.enums.SocialType;
import org.delivery.voda.domain.user.enums.UserRole;
import org.delivery.voda.domain.user.repository.UserRepository;
import org.delivery.voda.global.error.BusinessException;
import org.delivery.voda.global.error.ErrorCode;
import org.delivery.voda.security.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  @Transactional
  public Long signup(SignupRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
    }

    //비밀번호 암호화
    String encodedPassword = passwordEncoder.encode(request.getPassword());

    LocalDate birthday;
    try{
      birthday = LocalDate.parse(request.getBirthDate());
    }catch (Exception e) {
      throw  new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
    }

    User user = User.builder()
        .email(request.getEmail())
        .password(encodedPassword)
        .nickname(request.getNickname())
        .birthDate(birthday)
        .role(UserRole.USER)
        .socialType(SocialType.VODA)
        .build();

    User saveUser = userRepository.save(user);

    return saveUser.getId();

  }

  @Transactional
  public TokenResponse login(LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
      throw new BusinessException(ErrorCode.INVALID_PASSWORD);
    }

    String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().getKey());
    String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

    refreshTokenRepository.save(new RefreshToken(user.getEmail(), refreshToken));

    return  TokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .tokenType("Bearer")
        .build();

  }

}
