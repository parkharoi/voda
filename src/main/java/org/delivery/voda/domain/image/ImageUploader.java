package org.delivery.voda.domain.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {
  String upload(MultipartFile file, String dirName);

}
