package org.delivery.voda.domain.user.dto.respose;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
  private String accessToken;
  private String tokenType;
}
