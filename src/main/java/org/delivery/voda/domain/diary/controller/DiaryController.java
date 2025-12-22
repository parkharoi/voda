package org.delivery.voda.domain.diary.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.delivery.voda.domain.diary.dto.request.DiaryRequest;
import org.delivery.voda.domain.diary.dto.response.DiaryResponse;
import org.delivery.voda.domain.diary.dto.response.DiarySummaryResponse;
import org.delivery.voda.domain.diary.service.DiaryService;
import org.delivery.voda.domain.user.entity.User;
import org.delivery.voda.global.common.ApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diaries")
public class DiaryController {

  private final DiaryService diaryService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<ApiResponse<DiaryResponse>> create(
      @RequestPart(value = "data")@Valid DiaryRequest request,
      @RequestPart(value = "file", required = false)MultipartFile file,
      @AuthenticationPrincipal UserDetails userDetails

      ) {
    String email = userDetails.getUsername();
    DiaryResponse response = diaryService.createDiary(email, request, file);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  //월별 조회
  @GetMapping
  public ResponseEntity<ApiResponse<List<DiarySummaryResponse>>> getMonthlyDiaries(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestParam("year") int year,
      @RequestParam("month") int month
  ) {
    String email = userDetails.getUsername();

    List<DiarySummaryResponse> responses = diaryService.getMonthlyDiaries(email, year, month);
    return ResponseEntity.ok(ApiResponse.success(responses));
  }

  //일별 상세 조회
  @GetMapping("/{date}")
  public ResponseEntity<ApiResponse<DiaryResponse>> getDiaryDetail(
      @AuthenticationPrincipal UserDetails userDetails,
      @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
  ) {
    String email = userDetails.getUsername();

    DiaryResponse response = diaryService.getDiaryDetail(email, date);

    return ResponseEntity.ok(ApiResponse.success(response));
  }


  //ai요약 버튼 클릭 시
  @PostMapping("/ai")
  public ResponseEntity<ApiResponse<Void>> createDiary(
      @AuthenticationPrincipal UserDetails userDetails
  ) {
    String email = userDetails.getUsername();

    diaryService.createAiDiaryByEmail(email);

    return ResponseEntity.ok(ApiResponse.success());
  }
}
