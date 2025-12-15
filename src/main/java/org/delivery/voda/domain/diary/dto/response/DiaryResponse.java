package org.delivery.voda.domain.diary.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.delivery.voda.domain.diary.entity.Diary;
import org.delivery.voda.domain.diary.enums.DiaryType;
import org.delivery.voda.domain.diary.enums.Mood;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiaryResponse {

  private Long diaryId;
  private String title;
  private String description;
  private String imgUrl;
  private LocalDate writtenDate;
  private Mood mood;
  private String moodLabel;
  private DiaryType diaryType;

  public static DiaryResponse from(Diary diary) {
    return DiaryResponse.builder()
        .diaryId(diary.getId())
        .title(diary.getTitle())
        .description(diary.getDescription())
        .imgUrl(diary.getImgUrl())
        .writtenDate(diary.getWrittenDate())
        .mood(diary.getMood())
        .moodLabel(diary.getMood().getMood())
        .diaryType(diary.getDiaryType())
        .build();
  }

}
