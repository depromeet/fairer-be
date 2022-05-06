package com.depromeet.fairer.dto.housework;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HouseWorkDateResponseDto {

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate scheduledDate;

    private int countDone;
    private int countLeft;
    private List<HouseWorkResponseDto> houseWorks;

    public static HouseWorkDateResponseDto from(LocalDate scheduledDate, int countDone, int countLeft,
                                           List<HouseWorkResponseDto> houseWorkResponseDtos){
        return new HouseWorkDateResponseDtoBuilder()
                .scheduledDate(scheduledDate)
                .countDone(countDone)
                .countLeft(countLeft)
                .houseWorks(houseWorkResponseDtos)
                .build();
    }
}
