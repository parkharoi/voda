package org.delivery.voda.domain.diary.service;

import ch.qos.logback.core.spi.ErrorCodes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.voda.domain.chat.entity.Chat;
import org.delivery.voda.domain.chat.repository.ChatRepository;
import org.delivery.voda.domain.chat.service.GeminiService;
import org.delivery.voda.domain.diary.dto.request.DiaryRequest;
import org.delivery.voda.domain.diary.dto.response.DiaryResponse;
import org.delivery.voda.domain.diary.dto.response.DiarySummaryResponse;
import org.delivery.voda.domain.diary.entity.Diary;
import org.delivery.voda.domain.diary.enums.DiaryType;
import org.delivery.voda.domain.diary.enums.Mood;
import org.delivery.voda.domain.diary.repository.DiaryRepository;
import org.delivery.voda.domain.diary.util.AiDiaryParser;
import org.delivery.voda.domain.image.ImageUploader;
import org.delivery.voda.domain.user.entity.User;
import org.delivery.voda.domain.user.repository.UserRepository;
import org.delivery.voda.global.error.BusinessException;
import org.delivery.voda.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiaryService {

  private final DiaryRepository diaryRepository;
  private final UserRepository userRepository;
  private final ChatRepository chatRepository;
  private final GeminiService geminiService;
  private final ImageUploader imageUploader;
  private final AiDiaryParser aiDiaryParser;

  //수동 일기 작성
  public DiaryResponse createDiary(String email,DiaryRequest request, MultipartFile file) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    validateDiaryAlreadyExists(user, LocalDate.now());

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


  //AI 일기 작성
  @Transactional
  public void createAiDiary(User user) {
    LocalDate today = LocalDate.now();
    validateDiaryAlreadyExists(user, today);

    //채팅 기록 가져오기
    List<String> chatLogs = getChatLogsForToday(user, today);
    // AI에게 요청
    String aiResponse = geminiService.generateDiaryAndEmotion(chatLogs);
    // 응답 해석
    AiDiaryParser.AiResult result = aiDiaryParser.parse(aiResponse);
    // 저장
    saveAiDiary(user, result, today);
  }

  private List<String> getChatLogsForToday(User user, LocalDate today) {
    LocalDateTime startOfDay = today.atStartOfDay();
    LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

    List<Chat> chats = chatRepository.findAllByUserAndCreatedAtBetweenOrderByCreatedAtAsc(
        user, startOfDay, endOfDay);

    if(chats.isEmpty()) {
      throw new BusinessException(ErrorCode.CHAT_NOT_FOUND);
    }

    return chats.stream()
        .map(chat -> chat.getSender() + ": " + chat.getMessage())
        .collect(Collectors.toList());
  }

  // AI 응답 파싱 내용 저장
  private void saveAiDiary(User user, AiDiaryParser.AiResult result, LocalDate date) {
    Diary diary = Diary.builder()
        .user(user)
        .title(result.getTitle())
        .mood(result.getMood())
        .description(result.getContent())
        .writtenDate(date)
        .diaryType(DiaryType.AI_GENERATED)
        .imgUrl(null)
        .build();

    diaryRepository.save(diary);
    log.info("AI 일기 저장 성공: User ID={}, Date={}", user.getId(), date);
    }


  //일기 중복 검증 메서드
  private void validateDiaryAlreadyExists(User user, LocalDate date) {
    if(diaryRepository.existsByUserAndWrittenDate(user, date)) {
      throw  new BusinessException(ErrorCode.DIARY_ALREADY_EXISTS);
    }
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
