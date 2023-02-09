package com.depromeet.fairer.dto.houseworkComplete.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.YearMonth;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "팀 멤버 집안일 별 완료 통계 조회 요청 객체", description = "팀 멤버 집안일 별 완료 통계 조회 요청 객체")
public class MonthlyHouseWorkStaticsByHouseWorkRequestDto {

    @NotNull
    @ApiModelProperty(value = "조회 연월")
    @DateTimeFormat(pattern = "YYYY-MM")
    private YearMonth month;

    @NotNull
    @NotEmpty
    @ApiModelProperty(value = "집안일 이름")
    private String houseWorkName;
}


