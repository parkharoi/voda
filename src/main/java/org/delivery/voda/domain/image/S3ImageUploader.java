package org.delivery.voda.domain.image;

import io.awspring.cloud.s3.S3Template;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Profile("prod")
@Component
@RequiredArgsConstructor
public class S3ImageUploader implements ImageUploader{

  private final S3Template s3Template;

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucket;

  @Override
  public String upload(MultipartFile file, String dirName) {
    if (file.isEmpty()) return null;

    String originalFileName = file.getOriginalFilename();
    String uuid = UUID.randomUUID().toString();
    String storedFileName = dirName + "/" + uuid + "_" + originalFileName;

    try(InputStream inputStream = file.getInputStream()) {
      s3Template.upload(bucket, storedFileName, inputStream);
      return s3Template.download(bucket, storedFileName).getURL().toString();
    }catch (IOException e) {
      log.error("S3 업로드 실패", e);
      throw new RuntimeException("S3 업로드 중 에러가 발생했습니다.");
    }
  }
}
