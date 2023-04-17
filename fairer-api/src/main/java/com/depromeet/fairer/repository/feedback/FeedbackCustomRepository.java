package com.depromeet.fairer.repository.feedback;

import com.depromeet.fairer.domain.feedback.Feedback;

public interface FeedbackCustomRepository {

    Feedback getFeedback(Long houseworkCompleteId, Long memberId);
}
