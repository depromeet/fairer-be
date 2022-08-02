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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken());
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8");

            HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(fcmSendRequest), headers);
            Message message = restTemplate.postForObject(FCM_DOMAIN, request, Message.class);
            log.info("Send FCM Message : {}, request : {}", message, fcmMessageRequest);
            return FCMMessageResponse.of(fcmMessageRequest.getTitle(), fcmMessageRequest.getBody(), fcmMessageRequest.getMemberId());
        } catch (Exception e) {
            log.error("Error to send message.", e);
            throw new FairerException(e);
        }
    }

    private FCMSendRequest createMessage(String token, String title, String body) {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(new Notification(title, body))
                .build();

        FCMSendRequest request = new FCMSendRequest();
        request.setValidate_only(false);
        request.setMessage(message);
        return request;
    }

    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(FIREBASE_KEY_PATH).getInputStream()).createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}