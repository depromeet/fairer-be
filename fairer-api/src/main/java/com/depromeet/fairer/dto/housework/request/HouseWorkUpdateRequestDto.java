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

    @ApiModelProperty(value = "수정할 집안일 ID", required = true)
    @NotNull
    private Long houseWorkId;

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

//    @ApiModelProperty(value = "집안일 반복 요일", example = "월수")
//    private String repeatDayOfWeek;

    public HouseWork toEntity() {
        return HouseWork.builder()
                .space(space)
                .houseWorkName(houseWorkName)
                .scheduledDate(scheduledDate)
                .scheduledTime(scheduledTime)
                //.repeatDayOfWeek(repeatDayOfWeek)
                .repeatCycle(RepeatCycle.of(repeatCycle))
                .success(false)
                .successDateTime(null)
                .build();
    }

    @ApiModelProperty(value = "집안일 반복 패턴", example = "repeatCycle이 weekly일 경우: monday, sunday / monthly일 경우: 31")
    private String repeatPattern;

    @ApiModelProperty(value = "집안일 종료일", example = "2022-07-02", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate repeatEndDate;

    @ApiModelProperty(value = "수정할 집안일 기간", required = true, example = "단일 수정: 'O' / 앞으로 일정 수정: 'H' / 모두 수정: 'A'")
    @NotNull
    private String type;

    @ApiModelProperty(value = "수정 기준 날짜", example = "2022-07-02",
            notes = "주기에 해당하는 날짜여야 함, 수요일 주기일 경우 수정의 기준이 되는 원하는 마지막 수요일 날짜")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate updateStandardDate;
}