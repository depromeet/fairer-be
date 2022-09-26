package com.depromeet.fairer.domain.feedback;

import com.depromeet.fairer.domain.base.BaseTimeEntity;
import com.depromeet.fairer.domain.houseworkComplete.HouseworkComplete;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.service.feedback.FeedbackService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "feedback")
@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Feedback extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long feedbackId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_work_complete_id")
    private HouseworkComplete houseworkComplete;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    private Integer grade;

    private String comment;

    private Integer emoji;

    public boolean isCreator(Long memberId) {
        return this.member.getMemberId().equals(memberId);
    }

    public static Feedback create(HouseworkComplete houseworkComplete, Member member, String comment, int emoji) {
        final Feedback feedback = Feedback.builder()
                .houseworkComplete(houseworkComplete) //TODO HouseWorkComplete에 New ArrayList<> 만들기
                .member(member)
                .comment(comment)
                .emoji(emoji)
                .build();
        houseworkComplete.getFeedbackList().add(feedback);
        return feedback;
    }

}
