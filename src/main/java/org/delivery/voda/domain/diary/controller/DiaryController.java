package org.delivery.voda.domain.diary.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.delivery.voda.domain.diary.dto.request.DiaryRequest;
import org.delivery.voda.domain.diary.dto.response.DiaryResponse;
import org.delivery.voda.domain.diary.service.DiaryService;
import org.delivery.voda.global.common.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    Long userId = Long.parseLong(userDetails.getUsername());
    DiaryResponse response = diaryService.createDiary(userId, request, file);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

}
