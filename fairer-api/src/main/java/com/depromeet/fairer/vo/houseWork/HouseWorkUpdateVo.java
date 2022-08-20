package com.depromeet.fairer.vo.houseWork;

import com.depromeet.fairer.domain.preset.Space;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class HouseWorkUpdateVo {

    private Long memberId;

    private Long houseWorkId;

    private List<Long> assignees;

    private Space space;

    private String houseWorkName;

    private LocalDate scheduledDate;

    private LocalTime scheduledTime;
}
