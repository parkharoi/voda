package org.delivery.voda.domain.diary.repository;

import org.delivery.voda.domain.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

}
