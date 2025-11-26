package org.delivery.voda.domain.diary.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.delivery.voda.domain.diary.dto.request.DiaryRequest;
import org.delivery.voda.domain.diary.dto.response.DiaryResponse;
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

  public DiaryResponse createDiary(Long userId,DiaryRequest request, MultipartFile file) {
    User user = userRepository.findById(userId)
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
        .diaryTyoe(DiaryType.MANUAL)
        .build();

    Diary savedDiary = diaryRepository.save(diary);

    return DiaryResponse.from(savedDiary);

  }

}
