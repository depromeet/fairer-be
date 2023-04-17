package com.depromeet.fairer.service.feedback;

import com.depromeet.fairer.domain.feedback.Feedback;
import com.depromeet.fairer.domain.houseworkComplete.HouseworkComplete;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.global.exception.BadRequestException;
import com.depromeet.fairer.global.exception.NoSuchMemberException;
import com.depromeet.fairer.global.exception.PermissionDeniedException;
import com.depromeet.fairer.repository.feedback.FeedbackRepository;
import com.depromeet.fairer.repository.houseworkcomplete.HouseWorkCompleteRepository;
import com.depromeet.fairer.repository.member.MemberRepository;
import com.depromeet.fairer.vo.houseWork.HouseWorkCompFeedbackVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final HouseWorkCompleteRepository houseWorkCompleteRepository;
    private final MemberRepository memberRepository;

    public Long create(Long memberId, Long houseCompleteId, String comment, int emoji){

        HouseworkComplete houseworkComplete = findWithFeedbackListOrThrow(houseCompleteId);

        List<Feedback> feedbacks = houseworkComplete.getFeedbackList();
        for(Feedback fb : feedbacks){
            if(fb.getComment() != null){
                throw new BadRequestException("텍스트 피드백은 하나만 작성할 수 있습니다.");
            }
        }

        Member member = findMemberOrThrow(memberId);

        if (emoji > 7) throw new BadRequestException("이모지 입력이 잘못되었습니다.");

        Feedback feedback = Feedback.create(houseworkComplete, member, comment, emoji);
        return feedbackRepository.save(feedback).getFeedbackId();
    }

    public void delete(Long memberId, Long feedbackId) {
        final Feedback feedback = findFeedbackOrThrow(feedbackId);
        if (feedback.isCreator(memberId)) {
            feedbackRepository.deleteById(feedbackId);
        } else {
            throw new PermissionDeniedException("해당 피드백 삭제 권한이 없습니다.");
        }
    }

    private HouseworkComplete findWithFeedbackListOrThrow(Long houseCompleteId) {
        return houseWorkCompleteRepository.findWithFeedbackByHouseWorkCompleteId(houseCompleteId).orElseThrow(() -> {
            throw new BadRequestException("완료되지 않은 집안일입니다");
        });
    }

    private HouseworkComplete findHouseWorkCompleteOrThrow(Long houseCompleteId) {
        return houseWorkCompleteRepository.findById(houseCompleteId).orElseThrow(() -> {
            throw new BadRequestException("완료되지 않은 집안일입니다");
        });
    }

    private Feedback findFeedbackOrThrow(Long feedbackId) {
        return feedbackRepository.findById(feedbackId).orElseThrow(() -> {
            throw new BadRequestException("요청한 피드백은 존재하지 않습니다.");
        });
    }

    private Member findMemberOrThrow(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(() -> {
            throw new NoSuchMemberException("존재하지 않는 멤버입니다.");
        });
    }

    public Feedback modify(Long houseworkCompleteId, String comment, Long memberId) {

        Feedback feedback = feedbackRepository.getFeedback(houseworkCompleteId, memberId);
        if(feedback == null){
            throw new BadRequestException("존재하지 않는 피드백입니다.");
        }
        feedback.updateComment(comment);
        return feedback;
    }

    public Feedback find(Long feedbackId) {
        return findFeedbackOrThrow(feedbackId);
    }

    public List<HouseWorkCompFeedbackVO> findAll(Long houseWorkCompleteId) {
       // final List<Feedback> feedbackList = findWithFeedbackListAndMemberOrThrow(houseWorkCompleteId).getFeedbackList();
        final List<Feedback> feedbackList = feedbackRepository.findByHouseWorkCompleteId(houseWorkCompleteId);

        final List<HouseWorkCompFeedbackVO> VOList = new ArrayList<>();
        for (Feedback feedback : feedbackList) {
            final HouseWorkCompFeedbackVO VO = new HouseWorkCompFeedbackVO();
            VO.setMemberName(feedback.getMember().getMemberName());
            VO.setProfilePath(feedback.getMember().getProfilePath());
            VO.setComment(feedback.getComment());
            if(feedback.getEmoji() != null) {
                VO.setEmoji(feedback.getEmoji());
            } else {
                VO.setEmoji(0);
            }
            VOList.add(VO);
        }
        return VOList;
    }

    private HouseworkComplete findWithFeedbackListAndMemberOrThrow(Long houseWorkCompleteId) {
        return houseWorkCompleteRepository.findWithFeedbackAndMemberByHouseWorkCompleteId(houseWorkCompleteId).orElseThrow(() -> {
            throw new BadRequestException("완료되지 않은 집안일입니다");
        });
    }
}
