package org.delivery.voda.domain.chat.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.voda.domain.chat.dto.request.ChatRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {

  @Value("${gemini.api.key}")
  private String apiKey;

  public String getContents(ChatRequest request) {
    try {
      Client client = Client.builder().apiKey(apiKey).build();

      //  페르소나 설정 가져오기
      String instruction = getInstruction(request.getType());
      String finalPrompt = instruction + "\n\n[사용자 메시지]: " + request.getMessage();

      //  요청 보내기
      GenerateContentResponse response = client.models.generateContent(
          "gemini-2.5-flash",
          finalPrompt,
          null
      );

      // 응답 꺼내기
      return response.text();

    } catch (Exception e) {
      log.error("Gemini API 호출 에러", e);
      return "죄송해요, 대화 중 오류가 발생했어요: " + e.getMessage();
    }
  }


  // 감정 분석 메서드
  public String generateDiaryAndEmotion(List<String> chatHistory) {
    try {
      Client client = Client.builder().apiKey(apiKey).build();

      //채팅 기록 전체 합치기
      String conversationLog = String.join("\n", chatHistory);

      String prompt = "너는 사용자의 하루 대화를 분석해서 일기를 대신 써주는 'AI 서기'야.\n" +
          "아래 대화 기록을 바탕으로 다음 3가지 작업을 수행해줘.\n\n" +
          "1. [일기 작성]: \n" +
          "   - **반드시 '나'의 시점(1인칭)으로 작성해줘.** (예: '오늘 나는...', '친구랑 떡볶이를 먹었다.')\n" +
          "   - 사용자가 직접 쓴 것처럼 자연스러운 구어체로 작성해.\n" +
          "   - 분량은 3~5문장.\n\n" +
          "2. [감정 선택]: 다음 6가지 중 오늘 기분을 가장 잘 나타내는 것 하나를 골라.\n" +
          "   [보기: HAPPY, PEACE, SAD, ANXIETY, EXCITED, ANGRY]\n\n" +
          "3. [제목 작성]: 일기 내용을 한눈에 볼 수 있는 짧고 임팩트 있는 제목을 지어줘. (10자 이내)\n\n" +
          "응답 형식(반드시 지킬것):\n" +
          "TITLE: {제목}\n" +
          "MOOD: {감정키워드}\n" +
          "DIARY: {일기내용}\n\n" +
          "== 대화 기록 ==\n" +
          conversationLog;

      GenerateContentResponse response = client.models.generateContent(
          "gemini-2.5-flash",
          prompt,
          null
      );
      return response.text();
    }catch (Exception e) {
      log.error("제미나이 일기 생성 중 에러 발생", e);
      return "일기 생성에 실패했습니다.";
    }
  }


  // 캐릭터 설정 (프롬프트)
  private String getInstruction(int type) {
    switch (type) {
      case 1: // 친구
        return "너는 지금부터 사용자의 '가장 친한 친구'야. 반말을 사용하고, 친근하고 유쾌하게 대화해. " +
            "사용자가 끝말잇기를 하자고 하면 재미있게 받아줘. 이모티콘도 적절히 섞어줘.";
      case 2: // 상담가 (공감)
        return "너는 따뜻한 마음을 가진 '심리 상담가'야. 사용자의 말에 깊이 공감해주고, 위로가 되는 말을 존댓말로 해줘. " +
            "해결책보다는 감정을 읽어주는 데 집중해.";
      case 3: // 개그맨
        return "너는 웃음이 많은 '개그맨'이야. 모든 대답에 아재 개그나 드립을 섞어서 사용자를 웃겨줘. " +
            "너무 진지한 상황에서도 유머를 잃지 마.";
      case 4: // 팩트폭격기 (까칠)
        return "너는 논리적이고 까칠한 '현실주의자'야. 사용자의 고민에 대해 듣기 좋은 소리보다는 따끔한 현실적인 조언(팩트 폭력)을 해줘. " +
            "말투는 약간 냉소적이어야 해.";
      default:
        return "모든 대화는 길 필요 없어 짧게 한줄, 혹은 두 줄";
    }
  }
}