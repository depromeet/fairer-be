package com.depromeet.fairer.service.fcm;

import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.dto.fcm.FCMSendRequest;
import com.depromeet.fairer.dto.fcm.request.FCMMessageRequest;
import com.depromeet.fairer.dto.fcm.request.SaveTokenRequest;
import com.depromeet.fairer.dto.fcm.response.FCMMessageResponse;
import com.depromeet.fairer.dto.fcm.response.SaveTokenResponse;
import com.depromeet.fairer.global.exception.FairerException;
import com.depromeet.fairer.global.factory.RestTemplateFactory;
import com.depromeet.fairer.repository.member.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {
    private static final String FCM_DOMAIN = "https://fcm.googleapis.com/v1/projects/fairer-def59/messages:send";
    private static final String FIREBASE_KEY_PATH = "firebase/fairer-def59-firebase-adminsdk-uvxs2-2b35d6203d.json";
    private static final RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();
    private final ObjectMapper objectMapper;

    private final MemberRepository memberRepository;

    public SaveTokenResponse saveToken(SaveTokenRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("memberId에 해당하는 회원을 찾지 못했습니다."));
        member.setFcmToken(request.getToken());
        member.setFcmTokenDate(LocalDateTime.now());
        memberRepository.save(member);
        return SaveTokenResponse.of(request.getToken());
    }

    public FCMMessageResponse sendMessage(FCMMessageRequest fcmMessageRequest) {
        Member member = memberRepository.findById(fcmMessageRequest.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("memberId에 해당하는 회원을 찾지 못했습니다."));

        FCMSendRequest fcmSendRequest = createMessage(member.getFcmToken(), fcmMessageRequest.getTitle(), fcmMessageRequest.getBody());
        String body = convertFCMSendRequestToString(fcmSendRequest);
        this.sendFCMMessage(body);

        return FCMMessageResponse.of(fcmMessageRequest.getTitle(), fcmMessageRequest.getBody(), fcmMessageRequest.getMemberId());
    }

    private String convertFCMSendRequestToString(FCMSendRequest fcmSendRequest) {
        try {
            return objectMapper.writeValueAsString(fcmSendRequest);
        } catch (JsonProcessingException e) {
            throw new FairerException("객체 파싱 실패");
        }
    }

    private FCMSendRequest createMessage(String token, String title, String body) {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        return FCMSendRequest.of(message, false);
    }

    @Async(value = "fcmTaskExecutor")
    private void sendFCMMessage(String body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken());
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8");

            HttpEntity<String> request = new HttpEntity<>(body, headers);
            String message = restTemplate.postForObject(FCM_DOMAIN, request, String.class);
        } catch (Exception e) {
            log.error("Error to send message. body : {}", body, e);
        }
    }

    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(FIREBASE_KEY_PATH).getInputStream()).createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
