package com.depromeet.fairer.service.member.oauth;

import com.depromeet.fairer.domain.alarm.Alarm;
import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.member.constant.SocialType;
import com.depromeet.fairer.domain.memberToken.MemberToken;
import com.depromeet.fairer.dto.member.jwt.ResponseJwtTokenDto;
import com.depromeet.fairer.dto.member.jwt.TokenDto;
import com.depromeet.fairer.dto.member.oauth.OAuthAttributes;
import com.depromeet.fairer.dto.member.oauth.OauthLoginDto;
import com.depromeet.fairer.global.exception.BadRequestException;
import com.depromeet.fairer.global.exception.FairerException;
import com.depromeet.fairer.global.exception.MemberTokenNotFoundException;
import com.depromeet.fairer.global.exception.NoSuchMemberException;
import com.depromeet.fairer.repository.alarm.AlarmRepository;
import com.depromeet.fairer.repository.assignment.AssignmentRepository;
import com.depromeet.fairer.repository.housework.HouseWorkRepository;
import com.depromeet.fairer.repository.member.MemberRepository;
import com.depromeet.fairer.repository.memberToken.MemberTokenRepository;
import com.depromeet.fairer.service.member.jwt.TokenProvider;
import com.depromeet.fairer.service.member.oauth.google.GoogleFeignService;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.InvalidParameterException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OauthLoginService {

    private final GoogleFeignService googleFeignService;
    private final TokenProvider tokenProvider;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;
    private final AlarmRepository alarmRepository;
    private final AssignmentRepository assignmentRepository;
    private final HouseWorkRepository houseWorkRepository;

    @Value("${oauth2.clientId}")
    private String CLIENT_ID;

    @Value("${token.secret}")
    private String TOKEN_SECRET;

    public ResponseJwtTokenDto createMemberAndJwt(OauthLoginDto oauthLoginDto) {
        // 소셜 회원 정보 조회
        final OAuthAttributes socialUserInfo = getSocialUserInfo(oauthLoginDto);
        log.info("oauthAttributes: {}", socialUserInfo.toString());

        // 회원 가입 or 로그인
        Boolean hasTeam = false;
        Member requestMember;
        final Optional<Member> foundMember = memberRepository.findWithTeamByEmail(socialUserInfo.getEmail());
        if (foundMember.isEmpty()) { // 기존 회원 아닐 때
            Member newMember = Member.create(socialUserInfo);
            requestMember = memberRepository.save(newMember);
            alarmRepository.save(Alarm.create(requestMember));
        } else {
            requestMember = foundMember.get(); // 기존 회원일 때
            if (requestMember.getTeam() != null) {
                hasTeam = true;
            }
        }

        // JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.createTokenDto(requestMember.getMemberId());
        log.info("tokenDto: {}", tokenDto);

        ResponseJwtTokenDto responseJwtTokenDto = modelMapper.map(tokenDto, ResponseJwtTokenDto.class);
        final boolean isNewMember = StringUtils.isEmpty(requestMember.getMemberName());
        responseJwtTokenDto.setIsNewMember(isNewMember);
        if (!isNewMember) {
            responseJwtTokenDto.setMemberName(requestMember.getMemberName());
        }
        responseJwtTokenDto.setMemberId(requestMember.getMemberId());
        responseJwtTokenDto.setHasTeam(hasTeam);

        return responseJwtTokenDto;
    }

    private OAuthAttributes getSocialUserInfo(OauthLoginDto oauthLoginDto) {
        final String accessToken = oauthLoginDto.getAccessToken();
        final SocialType socialType = oauthLoginDto.getSocialType();
        return googleFeignService.getUserInfo(accessToken);
    }

    public String getAccessToken(String authorizationCode) {
        return googleFeignService.getAccess(authorizationCode);
    }

    public ResponseJwtTokenDto googleLoginIos(String tokenString) {

        Member requestMember;
        GoogleIdToken idToken = getVerifiedIdToken(tokenString);

        if (idToken == null) {
            throw new BadRequestException("Invalid token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        // Get profile information from payload
        String email = payload.getEmail();

        OAuthAttributes socialUserInfo = generateSocialInfoFromIdToken(idToken);

        log.info("oauthAttributes: {}", socialUserInfo.toString());

        final Optional<Member> foundMember = memberRepository.findWithTeamByEmail(email);

        if (foundMember.isEmpty()) { // 기존 회원 아닐 때
            Member newMember = Member.create(socialUserInfo);
            requestMember = memberRepository.save(newMember);
            alarmRepository.save(Alarm.create(requestMember));
        } else {
            requestMember = foundMember.get(); // 기존 회원일 때
        }

        // JWT 토큰 생성
        ResponseJwtTokenDto responseJwtTokenDto = generateToken(requestMember);

        return responseJwtTokenDto;
    }

    public ResponseJwtTokenDto loginAppleIos(String tokenString) throws JsonProcessingException {
        Member requestMember;

        String[] decodeArray = tokenString.split("\\.");
        String header = new String(Base64.getDecoder().decode(decodeArray[0]));

        //apple에서 제공해주는 kid값과 일치하는지 알기 위해
        JsonElement kid = ((JsonObject) JsonParser.parseString(header)).get("kid");
        JsonElement alg = ((JsonObject) JsonParser.parseString(header)).get("alg");

        PublicKey publicKey = this.getPublicKey(kid, alg);

        Claims userInfo = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(tokenString).getBody();

        // json 파싱 다시
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(userInfo);
        log.info("json: :::::::::" + jsonString);

        //JsonObject userInfoObject = JsonParser.parseString(userInfo.toString()).getAsJsonObject();
        JsonObject userInfoObject = (JsonObject) JsonParser.parseString(jsonString);

        JsonElement appleAlg = userInfoObject.get("email");
        String email = appleAlg.getAsString();

        OAuthAttributes socialUserInfo = OAuthAttributes
                                            .builder()
                                                .email(email) // 이메일 동의 x 경우
                                                .name("")
                                                .socialType(SocialType.APPLE)
                                            .build();

        log.info("oauthAttributes: {}", socialUserInfo.toString());

        final Optional<Member> foundMember = memberRepository.findByEmail(email);

        if (foundMember.isEmpty()) { // 기존 회원 아닐 때
            Member newMember = Member.create(socialUserInfo);
            requestMember = memberRepository.save(newMember);
            alarmRepository.save(Alarm.create(requestMember));
        } else {
            requestMember = foundMember.get(); // 기존 회원일 때
        }

        // JWT 토큰 생성

        return generateToken(requestMember);
    }

    private JsonArray getApplePublicKeys() {
        StringBuilder apiKey = new StringBuilder();
        try {
            URL url = new URL("https://appleid.apple.com/auth/keys");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";

            while ((line = br.readLine()) != null) {
                apiKey.append(line);
            }

            JsonObject keys = (JsonObject) JsonParser.parseString(apiKey.toString());

            return (JsonArray) keys.get("keys");
        } catch (IOException e) {
            throw new FairerException("URL 파싱 실패");
        }
    }

    public PublicKey getPublicKey(JsonElement kid, JsonElement alg) {

        JsonArray keys = this.getApplePublicKeys();

        JsonObject avaliableObject = null;

        for (int i = 0; i < keys.size(); i++) {
            JsonObject appleObject = (JsonObject) keys.get(i);
            JsonElement appleKid = appleObject.get("kid");
            JsonElement appleAlg = appleObject.get("alg");

            if (Objects.equals(appleKid, kid) && Objects.equals(appleAlg, alg)) {
                avaliableObject = appleObject;
                break;
            }
        }

        //일치하는 공개키 없음
        if (ObjectUtils.isEmpty(avaliableObject)) {
            throw new BadRequestException("유호하지 않은 토큰입니다.");
        }

        String nStr = avaliableObject.get("n").toString();
        String eStr = avaliableObject.get("e").toString();

        byte[] nBytes = Base64.getUrlDecoder().decode(nStr.substring(1, nStr.length() - 1));
        byte[] eBytes = Base64.getUrlDecoder().decode(eStr.substring(1, eStr.length() - 1));

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            return publicKey;
        } catch (Exception exception) {
            throw new FairerException("애플 로그인 퍼블릭 키를 불러오는데 실패했습니다.");
        }
    }

    public ResponseJwtTokenDto generateToken(Member member) {
        // JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.createTokenDto(member.getMemberId());
        log.info("tokenDto: {}", tokenDto);

        ResponseJwtTokenDto responseJwtTokenDto = modelMapper.map(tokenDto, ResponseJwtTokenDto.class);
        final boolean isNewMember = StringUtils.isEmpty(member.getMemberName());
        responseJwtTokenDto.setIsNewMember(isNewMember);
        if (!isNewMember) {
            responseJwtTokenDto.setMemberName(member.getMemberName());
        }
        responseJwtTokenDto.setMemberId(member.getMemberId());
        responseJwtTokenDto.setHasTeam(member.hasTeam());

        return responseJwtTokenDto;
    }

    public OAuthAttributes generateSocialInfoFromIdToken(GoogleIdToken idToken) {
        GoogleIdToken.Payload payload = idToken.getPayload();

        // Print user identifier
        String userId = payload.getSubject();

        // Get profile information from payload
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        return OAuthAttributes
                .builder()
                .email(StringUtils.isBlank(email) ? userId : email) // 이메일 동의 x 경우
                .name(name)
                .socialType(SocialType.GOOGLE)
                .build();
    }

    public GoogleIdToken getVerifiedIdToken(String idTokenString) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(
                    new ApacheHttpTransport(ApacheHttpTransport.newDefaultHttpClient()),
                    new GsonFactory()
                )
                .setAudience(Collections.singletonList(CLIENT_ID))
                .setIssuer("https://accounts.google.com")
                .build();
        try {
            return verifier.verify(idTokenString);
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }
    }

    public GoogleIdToken getGoogleIdToken(String idToken) {
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(UrlFetchTransport.getDefaultInstance(), jsonFactory)
                .setAudience(Collections.singletonList("CLIENT_ID"))
                .setIssuer("https://accounts.google.com")
                .build();
        try {
            return verifier.verify(idToken);
        } catch (Exception e) {
            log.error("Google Token Verify Error: {}", e.getMessage());
            throw new BadRequestException("Google Token Verify Error");
        }
    }

    public ResponseJwtTokenDto login(SocialType socialType, String accessToken) {
        final OauthLoginDto oauthLoginDto = OauthLoginDto.builder().accessToken(accessToken).socialType(socialType).build();
        return createMemberAndJwt(oauthLoginDto);
    }

    public void validateLoginParams(SocialType socialType, String accessToken) {
//        validateSocialType(socialType);
        validateAccessToken(accessToken);
    }

    private void validateSocialType(String socialType) {
        if (!EnumUtils.isValidEnumIgnoreCase(SocialType.class, socialType)) {
            throw new InvalidParameterException("잘못된 소셜 타입입니다. 'GOOGLE' 중에 입력해주세요.");
        }
    }

    private void validateAccessToken(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            throw new InvalidParameterException("Access 토큰값을 입력해주세요.");
        }
    }


    public void logout(String refreshToken, LocalDateTime now) {
        final MemberToken memberToken = memberTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new MemberTokenNotFoundException("해당 리프레시 토큰이 존재하지 않습니다."));
        memberToken.expire(now);
    }

    public void signOut(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchMemberException("해당 멤버가 존재하지 않습니다."));

        List<Assignment> memberAssignmentList = assignmentRepository.findAllByMember(member);

        List<HouseWork> beStoppedHousework = assignmentRepository.findAllHouseWorkByAssignmentIdInAndHasOnlyAssignee(
                memberAssignmentList.stream()
                            .map(Assignment::getAssignmentId)
                            .collect(Collectors.toList()
                        ));

        List<Long> beStoppedHouseworkIdList = beStoppedHousework.stream()
                .map(HouseWork::getHouseWorkId)
                .collect(Collectors.toList());

        houseWorkRepository.updateAllByHouseWorkIdSetRepeatEndDate(beStoppedHouseworkIdList, LocalDate.now().minusDays(1));

        assignmentRepository.deleteAllByMember(member);

        String defaultProfileUrl = "https://firebasestorage.googleapis.com/v0/b/fairer-def59.appspot.com/o/fairer-profile-images%2Fic_profile1.svg?alt=media&token=13ef5688-3e56-452d-9c63-763958427674";

        member.setEmail(member.getEmail() + "_deleted_at_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        member.setProfilePath(defaultProfileUrl);
        member.delete();

        memberTokenRepository.updateExpirationTimeByMemberId(memberId, LocalDateTime.now());
    }
}
