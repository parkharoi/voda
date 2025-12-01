package org.delivery.voda.domain.diary.dto.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import org.delivery.voda.domain.diary.entity.Diary;
import org.delivery.voda.domain.diary.enums.Mood;

@Getter
@Builder
public class DiarySummaryResponse {

  private Long diaryId;
  private String title;

  private LocalDate writtenDate;
  private Mood mood;
  private String moodLabel;

  public static DiarySummaryResponse from(Diary diary) {
    return DiarySummaryResponse.builder()
        .diaryId(diary.getId())
        .title(diary.getTitle())
        .writtenDate(diary.getWrittenDate())
        .mood(diary.getMood())
        .moodLabel(diary.getMood().getMood())
        .build();
  }

}
