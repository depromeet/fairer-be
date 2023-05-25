package com.depromeet.fairer.repository.feedback;

import com.depromeet.fairer.domain.feedback.Feedback;

import java.util.List;

public interface FeedbackCustomRepository {

    List<Feedback> getFeedback(Long houseworkCompleteId, Long memberId);
}
