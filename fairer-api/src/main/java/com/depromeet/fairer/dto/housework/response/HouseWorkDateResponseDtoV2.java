package com.depromeet.fairer.dto.housework.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HouseWorkDateResponseDtoV2 {
    private Long memberId;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate scheduledDate;

    private Long countDone;
    private Long countLeft;
    private List<HouseWorkResponseDtoV2> houseWorks;

    public static HouseWorkDateResponseDtoV2 from(Long memberId, LocalDate scheduledDate, Long countDone, Long countLeft,
                                                List<HouseWorkResponseDtoV2> houseWorkResponseDtos){
        return new HouseWorkDateResponseDtoV2Builder()
                .memberId(memberId)
                .scheduledDate(scheduledDate)
                .countDone(countDone)
                .countLeft(countLeft)
                .houseWorks(houseWorkResponseDtos)
                .build();
    }
}