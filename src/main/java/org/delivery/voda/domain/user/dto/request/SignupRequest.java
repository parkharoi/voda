package org.delivery.voda.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
  private String email;
  private String password;
  private String nickname;
  private String birthDate;
}
