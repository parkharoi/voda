package org.delivery.voda.global.scheduler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.voda.domain.diary.service.DiaryService;
import org.delivery.voda.domain.user.entity.User;
import org.delivery.voda.domain.user.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiaryScheduler {

  private final DiaryService diaryService;
  private final UserRepository userRepository;

  @Scheduled(cron = "0 0 0 * * *")
  public void autoGenerateAiDiary(){
    log.info("자동 ai 일기 생성 스케쥴러 시작");

    List<User> users = userRepository.findAll();

    int successCount = 0;

    for (User user : users) {
      try {
        diaryService.createAiDiary(user);
        successCount++;
      }catch (Exception e) {
        log.warn("유저(ID:{}) AI 일기 생성 패스 {}", user.getId(), e.getMessage());
      }
    }
    log.info("스케쥴러 완료. 총 {}명 중 {}명 성공 ", users.size(), successCount);
  }

}
