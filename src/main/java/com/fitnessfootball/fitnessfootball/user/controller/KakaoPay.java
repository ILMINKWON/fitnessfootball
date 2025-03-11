package com.fitnessfootball.fitnessfootball.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fitnessfootball.fitnessfootball.dto.UserDto;

import jakarta.servlet.http.HttpSession;

@Controller
public class KakaoPay {

    private static final String KAKAO_PAY_ADMIN_KEY = "f1749816af1b06176ff99b20f3a759e2"; // 카카오 페이 Admin 키
    private static final String CID = "TC0ONETIME"; // 테스트용 CID

    @PostMapping("/api/kakao/pay")
    public ResponseEntity<Map<String, Object>> requestKakaoPay(@RequestBody Map<String, Object> paymentInfo, HttpSession session) {
        Integer amount = (Integer) paymentInfo.get("amount");
        String orderId = (String) paymentInfo.get("orderId");
        UserDto userDto = (UserDto) session.getAttribute("user");

        // 카카오페이 API 호출을 위한 요청 파라미터 설정
        // 카카오페이 API 호출을 위한 요청 파라미터 설정
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("cid", CID);  // 결제 승인 요청을 위한 cid (TC0ONETIME: 테스트용)
        requestBody.add("partner_order_id", orderId);  // 주문 번호
        requestBody.add("partner_user_id", userDto.getName());  // 사용자 ID (실제 사용자 ID로 대체 필요)
        requestBody.add("item_name", "상품명");  // 상품명
        requestBody.add("quantity", "1");  // 수량 (문자열로 설정)
        requestBody.add("total_amount", amount.toString());  // 총 결제 금액
        requestBody.add("tax_free_amount", "0");  // 면세 금액 (필요시)
        requestBody.add("approval_url", "http://localhost:8000/kakaoPaySuccess");  // 결제 승인 시 리디렉션 URL
        requestBody.add("cancel_url", "http://localhost:8000/kakaoPayCancel");  // 결제 취소 시 리디렉션 URL
        requestBody.add("fail_url", "http://localhost:8000/kakaoPayFail");  // 결제 실패 시 리디렉션 URL

        // 카카오페이 API로 결제 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + KAKAO_PAY_ADMIN_KEY);
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // 카카오페이 결제 준비 API 호출
        String kakaoPayUrl = "https://kapi.kakao.com/v1/payment/ready";
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
            kakaoPayUrl,
            HttpMethod.POST,
            requestEntity,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        // 응답 출력
        Map<String, Object> response = responseEntity.getBody();
        System.out.println("카카오페이 API 응답: " + response);

        // 결제 URL 확인
        String redirectUrl = (String) response.get("next_redirect_pc_url");
        if (redirectUrl == null) {
            return ResponseEntity.ok(Map.of("success", false, "message", "카카오페이 결제 준비 실패"));
        }

        // 결과 반환
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("redirectUrl", redirectUrl);

        return ResponseEntity.ok(result);
            }

        @GetMapping("/kakaoPaySuccess")
        public String kakaoPaySuccess(@RequestParam("pg_token") String pgToken) {
            // 카카오페이 결제 성공 후 처리할 로직
            // 예: pg_token을 이용해 결제 승인 요청을 하고, 결과를 처리할 수 있습니다.
            // 예시로는 결제 정보 확인 후 DB 업데이트나 사용자 알림 등을 추가할 수 있습니다.
            System.out.println("카카오페이 승인 토큰: " + pgToken);
    

            // 결제 승인 로직을 수행한 후 결제 완료 페이지로 리다이렉트
            return "redirect:/fitnessfootball/mainPage";
        }

        @GetMapping("/kakaoPayCancel")
        public ResponseEntity<Map<String, Object>> kakaoPayCancel() {
            // 카카오페이 결제 취소 후 처리할 로직
            // 예: 취소 후 사용자에게 알림을 보내거나, UI에 취소 메시지를 표시할 수 있습니다.

            Map<String, Object> response = new HashMap<>();
            response.put("message", "결제 취소");
            return ResponseEntity.ok(response);
        }

        @GetMapping("/kakaoPayFail")
        public ResponseEntity<Map<String, Object>> kakaoPayFail() {
            // 카카오페이 결제 실패 후 처리할 로직
            // 예: 결제 실패 후 사용자에게 알림을 보낼 수 있습니다.

            Map<String, Object> response = new HashMap<>();
            response.put("message", "결제 실패");
            return ResponseEntity.ok(response);
        }

        @PostMapping("/api/kakao/pay/approve")
public ResponseEntity<Map<String, Object>> approveKakaoPay(
        @RequestParam("pg_token") String pgToken,
        HttpSession session) {
    
    UserDto userDto = (UserDto) session.getAttribute("user");

    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("cid", CID);
    requestBody.add("tid", (String) session.getAttribute("tid")); // 결제 요청 시 받은 tid 사용
    requestBody.add("partner_order_id", "주문번호"); 
    requestBody.add("partner_user_id", userDto.getName());
    requestBody.add("pg_token", pgToken);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "KakaoAK " + KAKAO_PAY_ADMIN_KEY);
    headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
        "https://kapi.kakao.com/v1/payment/approve",
        HttpMethod.POST,
        requestEntity,
        new ParameterizedTypeReference<Map<String, Object>>() {}
    );

    return ResponseEntity.ok(response.getBody());
}
}
