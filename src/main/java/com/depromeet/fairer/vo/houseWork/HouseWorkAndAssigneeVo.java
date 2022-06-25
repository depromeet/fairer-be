package com.depromeet.fairer.vo.houseWork;

import com.depromeet.fairer.domain.preset.constant.Space;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter @Setter
public class HouseWorkAndAssigneeVo {

    private Long houseWorkId;

    private Space space;

    private String houseWorkName;

    private List<MemberVo> assignees;

    private LocalTime scheduledTime;

    private LocalDateTime successDateTime;

    private Boolean success;

    @Getter @Setter
    public static class MemberVo{
        private Long memberId;
        private String memberName;
        private String profilePath;
    }
}
