package com.depromeet.fairer.dto.statistic.response.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.YearMonth;

@Getter
@Setter
@NoArgsConstructor
public class MonthlyHouseWorkStatisticRequestDto {

    @NotNull

    @ApiModelProperty(value = "조회 연월")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/yyyy")
    @DateTimeFormat(pattern = "YYYY-MM")
    private YearMonth month;

    @ApiModelProperty(value = "집안일 이름")
    private String houseWorkName;


}


