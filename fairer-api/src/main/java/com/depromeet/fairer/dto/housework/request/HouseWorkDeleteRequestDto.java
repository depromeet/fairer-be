package com.depromeet.fairer.dto.housework.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@ApiModel(value = "집안일 삭제 요청 객체", description = "집안일 삭제 요청 객체")
public class HouseWorkDeleteRequestDto {

    @ApiModelProperty(value = "삭제할 집안일 ID", required = true)
    @NotNull
    private Long houseWorkId;

    @ApiModelProperty(value = "삭제할 집안일 기간", required = true, example = "단일 삭제: 'O' / 앞으로 삭제: 'H' / 모두 삭제: 'A'")
    @NotNull
    private String type;

    @ApiModelProperty(value = "삭제 기준 날짜", example = "2022-07-02",
            notes = "주기에 해당하는 날짜여야 함, 수요일 주기일 경우 삭제의 기준이 되는 원하는 마지막 수요일 날짜")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate deleteStandardDate;
}
