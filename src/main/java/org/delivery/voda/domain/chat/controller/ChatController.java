package org.delivery.voda.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.delivery.voda.domain.chat.dto.request.ChatRequest;
import org.delivery.voda.domain.chat.dto.request.ChatResponse;
import org.delivery.voda.domain.chat.service.GeminiService;
import org.delivery.voda.global.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

  private final GeminiService geminiService;

  @PostMapping
  public ResponseEntity<ApiResponse<ChatResponse>> chat(@RequestBody ChatRequest request) {
    String reply = geminiService.getContents(request);

    ChatResponse chatResponse = new ChatResponse(reply);

    return  ResponseEntity.ok(ApiResponse.success(chatResponse));
  }

}
