package org.delivery.voda.domain.diary.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Mood {
  HAPPY("HAPPY", "행복해요"),
  PEACE("PEACE", "평온해요"),
  SAD("SAD", "슬퍼요"),
  ANXIETY("ANXIETY", "불안해요"),
  EXCITED("EXCITED", "신나요");

  private final String key;
  private final String mood;

  }
