package org.delivery.voda.domain.diary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.delivery.voda.domain.diary.enums.DiaryType;
import org.delivery.voda.domain.diary.enums.Mood;
import org.delivery.voda.domain.user.entity.User;

@Table(name = "diary")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Diary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "diary_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Enumerated(EnumType.STRING)
  private Mood mood;

  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  private LocalDate writtenDate;

  private String imgUrl;

  @Enumerated(EnumType.STRING)
  private DiaryType diaryType;

  @Builder
  public Diary(User user,Mood mood, String title, String description,
               LocalDate writtenDate, String imgUrl, DiaryType diaryTyoe){

    this.user = user;
    this.mood = mood;
    this.title = title;
    this.description = description;
    this.writtenDate = writtenDate;
    this.imgUrl = imgUrl;
    this.diaryType = diaryTyoe;
  }

}
