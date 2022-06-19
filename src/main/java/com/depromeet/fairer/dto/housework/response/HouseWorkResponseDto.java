package com.depromeet.fairer.dto.housework.response;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.preset.constant.Space;
import com.depromeet.fairer.dto.member.MemberDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseWorkResponseDto {
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate scheduledDate;

    @ApiModelProperty(value = "집안일 예약시간", example = "10:00")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime scheduledTime;

    @ApiModelProperty(value = "집안일 완료일자", example = "2022-07-02T09:00:00")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime successDateTime;

    @ApiModelProperty(value = "집안일 완료 여부")
    private Boolean success;

    public static HouseWorkResponseDto from(HouseWork houseWork, List<MemberDto> memberDtoList) {
        return new HouseWorkResponseDtoBuilder()
                .houseWorkId(houseWork.getHouseWorkId())
                .space(houseWork.getSpace())
                .houseWorkName(houseWork.getHouseWorkName())
                .assignees(memberDtoList)
                .scheduledDate(houseWork.getScheduledDate())
                .scheduledTime(houseWork.getScheduledTime())
                .successDateTime(houseWork.getSuccessDateTime())
                .success(houseWork.getSuccess())
                .build();
    }
}