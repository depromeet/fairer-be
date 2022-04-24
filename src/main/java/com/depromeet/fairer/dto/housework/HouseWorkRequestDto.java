package com.depromeet.fairer.dto.housework;

import com.depromeet.fairer.domain.housework.Space;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class HouseWorkRequestDto {
    private Space space;
    private String houseworkName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate scheduledDate;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime scheduledTime;
    private LocalDateTime successDateTime = null;
    private Boolean success = false;
}
