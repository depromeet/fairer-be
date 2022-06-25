package com.depromeet.fairer.vo.houseWork;

import com.depromeet.fairer.domain.preset.constant.Space;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class HouseWorkUpdateVo {

    private Long memberId;

    private Long houseWorkId;

    private List<Long> assignees;

    private Space space;

    private String houseWorkName;

    private LocalDate scheduledDate;

    private LocalTime scheduledTime;
}
