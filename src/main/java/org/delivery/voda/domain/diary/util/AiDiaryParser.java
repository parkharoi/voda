package org.delivery.voda.domain.diary.util;

import lombok.Builder;
import lombok.Getter;
import org.delivery.voda.domain.diary.enums.Mood;
import org.springframework.stereotype.Component;

@Component
public class AiDiaryParser {
  @Getter
  @Builder
  public static class AiResult {
    private String title;
    private Mood mood;
    private String content;
  }

  public AiResult parse(String aiResponse) {
    try {
      String title = extractValue(aiResponse, "TITLE:");
      String moodStr = extractValue(aiResponse, "MOOD:");
      String content = extractValue(aiResponse, "DIARY:");

      Mood mood = Mood.valueOf(moodStr.toUpperCase().trim());

      return AiResult.builder()
          .title(title)
          .mood(mood)
          .content(content)
          .build();
    }catch (Exception e) {
      throw new RuntimeException("AI 응답 파싱 실패", e);
    }
  }

  private String extractValue(String text, String key) {
    int startIndex = text.indexOf(key);


    if(startIndex == -1) return "";

    String temp = text.substring(startIndex + key.length()).trim();

    if (key.equals("DIARY:")) return temp;

    int nextLineIndex = temp.indexOf("\n");
    if(nextLineIndex != -1) {
      return temp.substring(0, nextLineIndex).trim();
    }
    return temp;
  }

}
