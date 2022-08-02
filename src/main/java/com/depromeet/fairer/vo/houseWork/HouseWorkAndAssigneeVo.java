package com.depromeet.fairer.vo.houseWork;

import com.depromeet.fairer.domain.preset.Space;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter @Setter
@Builder
public class HouseWorkAndAssigneeVo {

    private Long houseWorkId;

    private Space space;

    private String houseWorkName;

    private List<MemberVo> assignees;

    private LocalTime scheduledTime;

    private LocalDateTime successDateTime;

    private Boolean success;

    @Getter @Setter @Builder
    public static class MemberVo{
        private Long memberId;
        private String memberName;
        private String profilePath;
    }
}
