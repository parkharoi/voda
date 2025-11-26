package org.delivery.voda.domain.auth.dto.respose;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenResponse {
  private String accessToken;
  private String refreshToken;
  private String tokenType;
}
