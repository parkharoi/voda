package org.delivery.voda.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

  GUEST("ROLE_GUEST", "손님"),
  USER("USER_ROLE", "일반 사용자"),
  ADMIN("ADMIN_ROLE", "관리자");

  private final String key;
  private final String title;

}
