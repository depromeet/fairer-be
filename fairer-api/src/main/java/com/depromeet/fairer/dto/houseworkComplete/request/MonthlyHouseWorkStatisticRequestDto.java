package com.depromeet.fairer.dto.houseworkComplete.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.YearMonth;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "멤버별 이번달 완료 집안일 통계 요청 객체", description = "멤버별 이번달 완료 집안일 통계 요청 객체")
public class MonthlyHouseWorkStatisticRequestDto {

    @NotNull
    @ApiModelProperty(value = "조회 연월")
    @DateTimeFormat(pattern = "YYYY-MM")
    private YearMonth month;
}
