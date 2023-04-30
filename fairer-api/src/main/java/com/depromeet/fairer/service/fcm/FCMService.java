package com.depromeet.fairer.service.fcm;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.dto.fcm.FCMSendRequest;
import com.depromeet.fairer.dto.fcm.request.FCMMessageRequest;
import com.depromeet.fairer.dto.fcm.request.SaveTokenRequest;
import com.depromeet.fairer.dto.fcm.response.FCMMessageResponse;
import com.depromeet.fairer.dto.fcm.response.SaveTokenResponse;
import com.depromeet.fairer.dto.member.MemberDto;
import com.depromeet.fairer.global.exception.FairerException;
import com.depromeet.fairer.global.factory.RestTemplateFactory;
import com.depromeet.fairer.repository.housework.HouseWorkRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {
    private static final String FCM_DOMAIN = "https://fcm.googleapis.com/v1/projects/fairer-def59/messages:send";
    private static final String FIREBASE_KEY_PATH = "firebase/fairer-def59-firebase-adminsdk-uvxs2-2b35d6203d.json";
    private static final RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();
    private final ObjectMapper objectMapper;

    private final MemberRepository memberRepository;
    private final HouseWorkRepository houseWorkRepository;

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

        if(Objects.isNull(member.getFcmToken())) {
            throw new FairerException("FCM Token이 null 입니다. member id : " + member.getMemberId());
        }

        FCMSendRequest fcmSendRequest = createMessage(member.getFcmToken(), fcmMessageRequest.getTitle(), fcmMessageRequest.getBody());
        String body = convertFCMSendRequestToString(fcmSendRequest);
        this.sendFCMMessage(body);

        return FCMMessageResponse.of(fcmMessageRequest.getTitle(), fcmMessageRequest.getBody(), fcmMessageRequest.getMemberId());
    }

    public List<FCMMessageResponse> sendHurry(Long houseworkId) {

        // housework
        HouseWork houseWork = houseWorkRepository.findById(houseworkId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 집안일입니다."));

        List<Member> members = getAssignedMemberList(houseworkId);

        String[] sentences = {"아직 " + houseWork.getHouseWorkName() + "이 남아있어요. 서둘러 처리해주세요.",
                "오늘은 " + houseWork.getHouseWorkName() + " 하는 날이에요. " + houseWork.getHouseWorkName() + "을 해주세요!",
                "재촉알림이 왔어요!"};

        Random random = new Random();
        int index = random.nextInt(sentences.length);


        List<FCMMessageResponse> response = new ArrayList<>();
        for(Member member : members){

            if(Objects.isNull(member.getFcmToken())) {
                throw new FairerException("FCM Token이 null 입니다. member id : " + member.getMemberId());
            }
            FCMSendRequest fcmSendRequest = createMessage(member.getFcmToken(), "재촉하기", sentences[index]);
            String body = convertFCMSendRequestToString(fcmSendRequest);
            this.sendFCMMessage(body);
            response.add(FCMMessageResponse.of("재촉하기", sentences[index], member.getMemberId()));
        }

        return response;
    }


    private List<Member> getAssignedMemberList(Long houseWorkId) {
        return memberRepository.getMemberDtoListByHouseWorkId(houseWorkId);
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
