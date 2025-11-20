package org.delivery.voda.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType {
  KAKAO("kakao", "카카오"),
  GOOGLE("google", "구글"),
  APPLE("apple", "애플"),
  VODA("voda", "일반 회원");

  private final String key;
  private final String description;
}
