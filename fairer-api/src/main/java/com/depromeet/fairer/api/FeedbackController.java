package com.depromeet.fairer.api;

import com.depromeet.fairer.domain.feedback.Feedback;
import com.depromeet.fairer.dto.feedback.FeedbackFindAllResponseDto;
import com.depromeet.fairer.dto.feedback.request.FeedbackCreateRequestDto;
import com.depromeet.fairer.dto.feedback.request.FeedbackUpdateRequestDto;
import com.depromeet.fairer.dto.feedback.response.FeedbackCreateResponseDto;
import com.depromeet.fairer.dto.feedback.response.FeedbackFindOneResponseDto;
import com.depromeet.fairer.dto.feedback.response.FeedbackFindResponseDto;
import com.depromeet.fairer.dto.feedback.response.FeedbackUpdateResponseDto;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.feedback.FeedbackService;
import com.depromeet.fairer.vo.houseWork.HouseWorkCompFeedbackVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Feedback", description = "피드백 API")
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Tag(name = "Feedback")
    @ApiOperation(value = "피드백 생성 api")
    @PostMapping(value="")
    public ResponseEntity<FeedbackCreateResponseDto> createFeedback(@ApiIgnore @RequestMemberId Long memberId,
                                                                    @RequestBody @Valid FeedbackCreateRequestDto req){
        final Long feedbackId = feedbackService.create(memberId, req.getHouseCompleteId(), req.getComment(), req.getEmoji());
        return new ResponseEntity<>(FeedbackCreateResponseDto.create(feedbackId), HttpStatus.CREATED);
    }

    @Tag(name = "Feedback")
    @ApiOperation(value = "피드백 삭제 api")
    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<?> deleteFeedback(@ApiIgnore @RequestMemberId Long memberId,
                                            @PathVariable("feedbackId") Long feedbackId) {
        feedbackService.delete(memberId, feedbackId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Tag(name = "Feedback")
    @ApiOperation(value = "피드백 수정 api")
    @PatchMapping("/{feedbackId}")
    public ResponseEntity<FeedbackUpdateResponseDto> modifyFeedback(@ApiIgnore @RequestMemberId Long memberId,
                                            @PathVariable("feedbackId") Long feedbackId,
                                            @RequestBody @Valid FeedbackUpdateRequestDto req){

        Feedback feedback = feedbackService.modify(feedbackId, req.getComment(), req.getEmoji());
        return ResponseEntity.ok(FeedbackUpdateResponseDto.from(feedback));
    }

    @Tag(name = "Feedback")
    @ApiOperation(value = "피드백 단건 조회 api")
    @GetMapping("/{feedbackId}")
    public ResponseEntity<FeedbackFindResponseDto> getFeedback(@PathVariable("feedbackId") Long feedbackId) {
        Feedback feedback = feedbackService.find(feedbackId);
        return ResponseEntity.ok(FeedbackFindResponseDto.from(feedback));
    }

    @Tag(name = "Feedback")
    @ApiOperation(value = "완료한 집안일의 모든 피드백 조회 api")
    @GetMapping("/houseworks/{houseWorkCompleteId}")
    public ResponseEntity<FeedbackFindAllResponseDto> getAllFeedbacks(@PathVariable("houseWorkCompleteId") Long houseWorkCompleteId) {
        final List<HouseWorkCompFeedbackVO> feedbackVOList = feedbackService.findAll(houseWorkCompleteId);

        final List<FeedbackFindOneResponseDto> responseDtoList = feedbackVOList.stream().map(FeedbackFindOneResponseDto::fromVO).collect(Collectors.toList());
        return ResponseEntity.ok(FeedbackFindAllResponseDto.from(responseDtoList));
    }

    //TODO Read는 FeedbackController와 HouseWorkController 모두 구현 필요

}
