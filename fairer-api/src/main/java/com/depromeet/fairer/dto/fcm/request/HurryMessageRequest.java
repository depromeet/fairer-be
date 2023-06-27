package com.depromeet.fairer.dto.fcm.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class HurryMessageRequest {

    @NotNull
    @ApiModelProperty(value = "집안일 id", required = true)
    private Long houseworkId;

    @NotNull
    @ApiModelProperty(value = "재촉할 집안일 일자", example = "2022-07-02", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate scheduledDate;

}
