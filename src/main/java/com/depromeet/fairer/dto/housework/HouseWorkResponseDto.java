package com.depromeet.fairer.dto.housework;

import com.depromeet.fairer.domain.housework.Housework;
import com.depromeet.fairer.domain.housework.Space;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class HouseWorkResponseDto {
    private Long id;
    private Space space;
    private String houseworkName;

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

    public static HouseWorkResponseDto from(Housework houseWork) {
        return new HouseWorkResponseDtoBuilder()
                .id(houseWork.getHouseworkId())
                .space(houseWork.getSpace())
                .houseworkName(houseWork.getHouseworkName())
                .scheduledDate(houseWork.getScheduledDate())
                .scheduledTime(houseWork.getScheduledTime())
                .successDateTime(houseWork.getSuccessDateTime())
                .success(houseWork.getSuccess())
                .build();
    }
}