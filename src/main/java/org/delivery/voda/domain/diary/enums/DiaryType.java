package org.delivery.voda.domain.diary.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiaryType {
  MANUAL("사용자 생성"),
  AI_GENERATED("AI 자동 생성");

  private final String diaryType;
}


