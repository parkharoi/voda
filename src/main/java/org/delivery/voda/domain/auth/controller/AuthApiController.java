package org.delivery.voda.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.delivery.voda.domain.auth.service.AuthService;
import org.delivery.voda.domain.auth.dto.request.LoginRequest;
import org.delivery.voda.domain.auth.dto.request.SignupRequest;
import org.delivery.voda.domain.auth.dto.respose.TokenResponse;
import org.delivery.voda.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthApiController {

  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse<Long>> Signup(@RequestBody @Valid SignupRequest request) {
    Long userId = authService.signup(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(userId));
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody @Valid LoginRequest request) {
    TokenResponse token = authService.login(request);
    return ResponseEntity.ok(ApiResponse.success(token));
  }

}
