package com.depromeet.fairer.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ApiModel(value = "처리 결과 반환 객체", description = "처리 결과 반환 객체")
public class CommonApiResult {

    @ApiModelProperty(value = "처리 결과 코드")
    private int code;

    @ApiModelProperty(value = "처리 결과 메세지")
    private String message;

    public static CommonApiResult createOk(){
        return CommonApiResult.builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .build();
    }

    public static CommonApiResult createOk(String message){
        return CommonApiResult.builder()
                .code(HttpStatus.OK.value())
                .message(message)
                .build();
    }
}
