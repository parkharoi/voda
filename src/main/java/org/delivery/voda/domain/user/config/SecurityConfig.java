package org.delivery.voda.domain.user.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests((auth) -> auth
        .requestMatchers("/", "/error", "/api/v1/auth/**").permitAll()
        .requestMatchers("/static/**", "/images/**").permitAll()
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated()
    );

    // CSRF 비활성화
    // REST API 서버는 세션 기반이 아닌 토큰(JWT) 등을 주로 쓰므로 CSRF를 끕니다.
    // (활성화하면 POST 요청 보낼 때마다 CSRF 토큰을 같이 보내야 해서 개발이 힘듭니다.)
    http.csrf((auth) -> auth.disable());

    http.formLogin((auth) -> auth.disable());

    http.httpBasic((auth) -> auth.disable());

    return http.build();
  }
}