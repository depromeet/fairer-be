package com.depromeet.fairer.vo.houseWorkComplete;

import com.depromeet.fairer.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseWorkCompleteStatisticsVo {

    private Member member;

    private Long completeCount;
}
