package com.depromeet.fairer.dto.statistic.response.request;

import com.depromeet.fairer.global.exception.BadRequestException;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.YearMonth;

@Getter
@Setter
@NoArgsConstructor
public class MonthlyHouseWorkStatisticRequestDto {

    @NotNull
    @ApiModelProperty(value = "조회 연월")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM")
    @DateTimeFormat(pattern = "YYYY-MM")
    private LocalDate month;

    @ApiModelProperty(value = "집안일 이름")
    private String houseWorkName;


    public void setHouseWorkName(String houseWorkName) {
        this.houseWorkName = houseWorkName;
    }

    public void setMonth(String month) {
        try {
            YearMonth.parse(month);
        } catch (Exception e) {
            throw new BadRequestException("올바르지 않은 날짜 형식입니다.");
        }
        this.month = LocalDate.parse(month + "-01");
    }
}


