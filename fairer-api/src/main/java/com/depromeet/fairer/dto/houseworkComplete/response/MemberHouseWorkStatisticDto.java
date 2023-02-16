package com.depromeet.fairer.dto.houseworkComplete.response;

import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.dto.member.MemberDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "멤버별 월별 완료한 집안일 통계 정보 반환 객체")
public class MemberHouseWorkStatisticDto {

    @ApiModelProperty(value = "멤버 정보")
    private MemberDto member;

    @ApiModelProperty(value = "멤버가 월별 완료한 집안일 개수")
    private Long houseWorkCount;

    public static MemberHouseWorkStatisticDto of(Member member, Long houseWorkCount) {
        return MemberHouseWorkStatisticDto.builder()
                .member(MemberDto.from(member))
                .houseWorkCount(houseWorkCount)
                .build();
    }
}
