package com.depromeet.fairer.dto.housework.request;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.preset.constant.Space;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class HouseWorkRequestDto {
    @NotNull
    private List<Long> assignees;

    @NotNull
    private Space space;

    @NotNull
    private String houseWorkName;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate scheduledDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime scheduledTime;

    public HouseWork toEntity() {
        return HouseWork.builder()
                .space(space)
                .houseWorkName(houseWorkName)
                .scheduledDate(scheduledDate)
                .scheduledTime(scheduledTime)
                .success(false)
                .successDateTime(null)
                .build();
    }
}

