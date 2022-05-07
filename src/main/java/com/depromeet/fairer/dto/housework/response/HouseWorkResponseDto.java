package com.depromeet.fairer.dto.housework.response;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.preset.constant.Space;
import com.depromeet.fairer.dto.member.MemberDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class HouseWorkResponseDto {
    private Long houseWorkId;
    private Space space;
    private String houseWorkName;
    private List<MemberDto> assignees;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate scheduledDate;

    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime scheduledTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime successDateTime;

    private Boolean success;

    public static HouseWorkResponseDto from(HouseWork houseWork, List<MemberDto> memberDtoList) {
        return new HouseWorkResponseDtoBuilder()
                .houseWorkId(houseWork.getHouseWorkId())
                .space(houseWork.getSpace())
                .houseWorkName(houseWork.getHouseWorkName())
                .assignees(memberDtoList)
                .scheduledDate(houseWork.getScheduledDate())
                .scheduledTime(houseWork.getScheduledTime())
                .successDateTime(houseWork.getSuccessDateTime())
                .success(houseWork.getSuccess())
                .build();
    }
}