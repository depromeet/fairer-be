package com.depromeet.fairer.dto.housework.response;


import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.preset.Space;
import com.depromeet.fairer.dto.feedback.response.FeedbackCountResponseDto;
import com.depromeet.fairer.dto.member.MemberDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseWorkResponseDtoV2 {
    @ApiModelProperty(value = "집안일 ID", example = "1")
    private Long houseWorkId;

    @ApiModelProperty(value = "공간", example = "KITCHEN")
    private Space space;

    @ApiModelProperty(value = "집안일 이름", example = "설거지")
    private String houseWorkName;

    @ApiModelProperty(value = "집안일 담당자 목록")
    private List<MemberDto> assignees;

    @ApiModelProperty(value = "집안일 예약일자", example = "2022-07-02")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate scheduledDate;

    @ApiModelProperty(value = "집안일 예약시간", example = "10:00")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime scheduledTime;

    @ApiModelProperty(value = "집안일 완료일자", example = "2022-07-02T09:00:00")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime successDateTime;

    @ApiModelProperty(value = "집안일 완료 여부")
    private Boolean success;

    @ApiModelProperty(value = "집안일 반복 주기", example = "O / D / W / M", notes = "단일: O, 매일: D, 주마다: W, 달마다: M")
    private String repeatCycle;

    @ApiModelProperty(value = "집안일 반복 요일", example = "repeatCycle이 weekly일 경우: monday, sunday / monthly일 경우: 31")
    private String repeatPattern;

    @ApiModelProperty(value = "집안일 종료일", example = "2022-07-02")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate repeatEndDate;

    @ApiModelProperty(value = "집안일 완료 ID")
    private Long houseWorkCompleteId;

    @ApiModelProperty(value = "피드백 이모지 별 개수")
    private FeedbackCountResponseDto feedbackCountResponseDto;

    public static HouseWorkResponseDtoV2 from(HouseWork houseWork, List<MemberDto> memberDtoList) {
        return new HouseWorkResponseDtoV2Builder()
                .houseWorkId(houseWork.getHouseWorkId())
                .space(houseWork.getSpace())
                .houseWorkName(houseWork.getHouseWorkName())
                .assignees(memberDtoList)
                .scheduledDate(houseWork.getScheduledDate())
                .scheduledTime(houseWork.getScheduledTime())
                .successDateTime(houseWork.getSuccessDateTime())
                .success(houseWork.getSuccess())
                .repeatCycle(houseWork.getRepeatCycle().getAlias())
                .repeatPattern(houseWork.getRepeatPattern())
                .repeatEndDate(houseWork.getRepeatEndDate())
                .build();
    }

    public static HouseWorkResponseDtoV2 from(HouseWork houseWork, List<MemberDto> memberDtoList, LocalDate date) {
        return new HouseWorkResponseDtoV2Builder()
                .houseWorkId(houseWork.getHouseWorkId())
                .space(houseWork.getSpace())
                .houseWorkName(houseWork.getHouseWorkName())
                .assignees(memberDtoList)
                .scheduledDate(date)
                .scheduledTime(houseWork.getScheduledTime())
                .successDateTime(houseWork.getSuccessDateTime())
                .success(houseWork.getSuccess())
                .build();
    }

    // 피드백 추가
    public static HouseWorkResponseDtoV2 from(HouseWork houseWork, List<MemberDto> memberDtoList, LocalDate date, Long houseWorkCompleteId,
                                              FeedbackCountResponseDto feedbackCountResponseDto) {

        if (houseWorkCompleteId != null){
            return new HouseWorkResponseDtoV2Builder()
                    .houseWorkId(houseWork.getHouseWorkId())
                    .space(houseWork.getSpace())
                    .houseWorkName(houseWork.getHouseWorkName())
                    .assignees(memberDtoList)
                    .scheduledDate(date)
                    .scheduledTime(houseWork.getScheduledTime())
                    .successDateTime(houseWork.getSuccessDateTime())
                    .success(true)
                    .houseWorkCompleteId(houseWorkCompleteId)
                    .feedbackCountResponseDto(feedbackCountResponseDto)
                    .build();
        }
        else {
            return new HouseWorkResponseDtoV2Builder()
                    .houseWorkId(houseWork.getHouseWorkId())
                    .space(houseWork.getSpace())
                    .houseWorkName(houseWork.getHouseWorkName())
                    .assignees(memberDtoList)
                    .scheduledDate(date)
                    .scheduledTime(houseWork.getScheduledTime())
                    .successDateTime(houseWork.getSuccessDateTime())
                    .success(false)
                    .houseWorkCompleteId(0L)
                    .feedbackCountResponseDto(feedbackCountResponseDto)
                    .build();
        }

    }


}