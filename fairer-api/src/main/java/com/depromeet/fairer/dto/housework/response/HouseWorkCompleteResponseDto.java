package com.depromeet.fairer.dto.housework.response;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.houseworkComplete.HouseworkComplete;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseWorkCompleteResponseDto {

    private Long houseWorkCompleteId;

    private HouseWork houseWork;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate scheduledDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime successDateTime;

    public static HouseWorkCompleteResponseDto from(HouseworkComplete houseworkComplete){
        return new HouseWorkCompleteResponseDtoBuilder()
                .houseWorkCompleteId(houseworkComplete.getHouseWorkCompleteId())
                .houseWork(houseworkComplete.getHouseWork())
                .scheduledDate(houseworkComplete.getScheduledDate())
                .successDateTime(houseworkComplete.getSuccessDateTime())
                .build();
    }
}

