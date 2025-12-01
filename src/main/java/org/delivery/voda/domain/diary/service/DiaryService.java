package org.delivery.voda.domain.diary.service;

import ch.qos.logback.core.spi.ErrorCodes;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.delivery.voda.domain.diary.dto.request.DiaryRequest;
import org.delivery.voda.domain.diary.dto.response.DiaryResponse;
import org.delivery.voda.domain.diary.dto.response.DiarySummaryResponse;
import org.delivery.voda.domain.diary.entity.Diary;
import org.delivery.voda.domain.diary.enums.DiaryType;
import org.delivery.voda.domain.diary.repository.DiaryRepository;
import org.delivery.voda.domain.image.ImageUploader;
import org.delivery.voda.domain.user.entity.User;
import org.delivery.voda.domain.user.repository.UserRepository;
import org.delivery.voda.global.error.BusinessException;
import org.delivery.voda.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DiaryService {

  private final DiaryRepository diaryRepository;
  private final UserRepository userRepository;
  private final ImageUploader imageUploader;

  public DiaryResponse createDiary(String email,DiaryRequest request, MultipartFile file) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    String imgUrl = null;
    if(file != null && !file.isEmpty()) {
      imgUrl = imageUploader.upload(file, "diary");
    }

    Diary diary = Diary.builder()
        .user(user)
        .title(request.getTitle())
        .mood(request.getMood())
        .description(request.getDescription())
        .imgUrl(imgUrl)
        .writtenDate(LocalDate.now())
        .diaryType(DiaryType.MANUAL)
        .build();

    Diary savedDiary = diaryRepository.save(diary);

    return DiaryResponse.from(savedDiary);

  }

  //해당 달 간단 내용
  public List<DiarySummaryResponse> getMonthlyDiaries(String email, int year, int month) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    YearMonth yearMonth = YearMonth.of(year, month);
    LocalDate startDate = yearMonth.atDay(1);
    LocalDate endDate = yearMonth.atEndOfMonth();


    List<Diary> diaries = diaryRepository.findAllByUserAndWrittenDateBetween(user, startDate, endDate);

    return diaries.stream()
        .map(DiarySummaryResponse::from)
        .collect(Collectors.toList());
  }

  // 일별 상세 조회 (해당 날짜 요청 시 전체 데이터)
  public DiaryResponse getDiaryDetail(String email, LocalDate date) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    Diary diary = diaryRepository.findByUserAndWrittenDate(user, date)
        .orElseThrow(() -> new BusinessException(ErrorCode.DIARY_NOT_FOUND));

    return DiaryResponse.from(diary);
  }



}
