package com.depromeet.fairer.dto.housework.request;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.housework.constant.RepeatCycle;
import com.depromeet.fairer.domain.preset.Space;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@ApiModel(value = "집안일 업데이트 요청 객체", description = "집안일 업데이트 요청 객체")
public class HouseWorkUpdateRequestDto {

    @ApiModelProperty(value = "집안일 담당자 목록", example = "[1, 13] (동일한 그룹 내 유저 id만 가능)", required = true)
    @NotNull
    private List<Long> assignees;

    @ApiModelProperty(value = "공간", example = "KITCHEN", required = true)
    @NotNull
    private Space space;

    @ApiModelProperty(value = "집안일 이름", example = "설거지", required = true)
    @NotNull
    private String houseWorkName;

    @ApiModelProperty(value = "집안일 예약일자", example = "2022-07-02", required = true)
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate scheduledDate;

    @ApiModelProperty(value = "집안일 예약시간", example = "10:00", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime scheduledTime;

    @ApiModelProperty(value = "집안일 반복 주기", example = "O / D / W / M", notes = "단일: O, 매일: D, 주마다: W, 달마다: M")
    private String repeatCycle;

    @ApiModelProperty(value = "집안일 반복 요일", example = "repeatCycle이 weekly일 경우: monday, sunday / monthly일 경우: 31")
    private String repeatPattern;

    public HouseWork toEntity() {
        return HouseWork.builder()
                .space(space)
                .houseWorkName(houseWorkName)
                .scheduledDate(scheduledDate)
                .scheduledTime(scheduledTime)
                .repeatPattern(repeatPattern)
                .repeatCycle(RepeatCycle.of(repeatCycle))
                .success(false)
                .successDateTime(null)
                .build();
    }
}

