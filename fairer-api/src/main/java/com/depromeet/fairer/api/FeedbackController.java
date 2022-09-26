package com.depromeet.fairer.api;

import com.depromeet.fairer.dto.feedback.request.FeedbackCreateRequestDto;
import com.depromeet.fairer.dto.feedback.response.FeedbackCreateResponseDto;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.feedback.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping(value="")
    public ResponseEntity<FeedbackCreateResponseDto> createFeedback(@ApiIgnore @RequestMemberId Long memberId,
                                                                    @RequestBody @Valid FeedbackCreateRequestDto req){
        final Long feedbackId = feedbackService.create(memberId, req.getHouseCompleteId(), req.getComment(), req.getEmoji());
        return new ResponseEntity<>(FeedbackCreateResponseDto.create(feedbackId), HttpStatus.CREATED);
    }

    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<?> deleteFeedback(@ApiIgnore @RequestMemberId Long memberId,
                                            @PathVariable("feedbackId") Long feedbackId) {
        feedbackService.delete(memberId, feedbackId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //TODO Read는 FeedbackController와 HouseWorkController 모두 구현 필요
}
