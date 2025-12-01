package org.delivery.voda.domain.diary.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.delivery.voda.domain.diary.entity.Diary;
import org.delivery.voda.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

  List<Diary> findAllByUserAndWrittenDateBetween(User user, LocalDate startDate, LocalDate endDate);

  Optional<Diary> findByUserAndWrittenDate(User user, LocalDate date);
}
