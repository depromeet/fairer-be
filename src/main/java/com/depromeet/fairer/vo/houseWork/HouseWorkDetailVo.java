package com.depromeet.fairer.vo.houseWork;

import com.depromeet.fairer.domain.preset.Space;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class HouseWorkDetailVo {

    private Long houseWorkId;

    private Space space;

    private String houseWorkName;

    private LocalTime scheduledTime;

    private LocalDateTime successDateTime;

    private Boolean success;

    private Long memberId;
    private String memberName;
    private String profilePath;
}
