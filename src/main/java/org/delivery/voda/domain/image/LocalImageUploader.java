package org.delivery.voda.domain.image;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Profile("local")
@Component
public class LocalImageUploader implements ImageUploader{

  private final String localPath = System.getProperty("user.dir") + "/files/";

  @Override
  public String upload(MultipartFile file, String dirName) {
    if (file.isEmpty()) return null;

    String uuid = UUID.randomUUID().toString();
    String fileName = uuid + "_" + file.getOriginalFilename();
    String fullPath = localPath + dirName + "/" + fileName;

    try {
      File folder = new File(localPath + dirName);
      if(!folder.exists()) folder.mkdirs();

      file.transferTo(new File(fullPath));

      return "/local-images/" + dirName + "/" + fileName;
    }catch (IOException e) {
      log.error("로컬 업로드 실패", e);
      throw new RuntimeException("로컬 파일 저장 실패");
    }
  }
}
