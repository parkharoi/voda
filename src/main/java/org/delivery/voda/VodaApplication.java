package org.delivery.voda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class VodaApplication {

  public static void main(String[] args) {
    SpringApplication.run(VodaApplication.class, args);
  }

}
