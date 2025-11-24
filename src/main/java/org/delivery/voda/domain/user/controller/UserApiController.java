package org.delivery.voda.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.delivery.voda.domain.user.dto.request.LoginRequest;
import org.delivery.voda.domain.user.dto.request.SignupRequest;
import org.delivery.voda.domain.user.dto.respose.TokenResponse;
import org.delivery.voda.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserApiController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<String> Signup(@RequestBody SignupRequest request) {
    userService.signup(request);
    return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
    TokenResponse token = userService.login(request);
    return ResponseEntity.ok(token);
  }

}
