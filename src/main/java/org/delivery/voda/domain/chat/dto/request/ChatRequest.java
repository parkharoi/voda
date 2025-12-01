package org.delivery.voda.domain.chat.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRequest {
  private String message;
  private int type;

}
