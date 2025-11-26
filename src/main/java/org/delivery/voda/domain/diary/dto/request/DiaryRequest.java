package org.delivery.voda.domain.diary.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.delivery.voda.domain.diary.enums.Mood;

@Getter
@Setter
@NoArgsConstructor
public class DiaryRequest {

  @NotBlank(message = "제목은 필수입니다.")
  private String title;

  @NotNull(message = "기분은 필수입니다.")
  private Mood mood;

  private String description;

}
