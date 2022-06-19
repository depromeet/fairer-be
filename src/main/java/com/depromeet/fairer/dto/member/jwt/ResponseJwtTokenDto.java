package com.depromeet.fairer.dto.member.jwt;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

@Getter @Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
public class ResponseJwtTokenDto {

    @ApiModelProperty(value = "access token")
    private String accessToken;

    @ApiModelProperty(value = "access token 만료 시간")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date accessTokenExpireTime;

    @ApiModelProperty(value = "refresh token")
    private String refreshToken;

    @ApiModelProperty(value = "refresh token 만료 시간")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date refreshTokenExpireTime;

    @ApiModelProperty(value = "신규 회원 여부")
    private Boolean isNewMember = false;

    @ApiModelProperty(value = "팀 소속 여부")
    private Boolean hasTeam = false;

    @ApiModelProperty(value = "멤버 이름")
    private String memberName;
}
