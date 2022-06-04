package com.depromeet.fairer.service.member;

import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.domain.member.Member;

import com.depromeet.fairer.global.exception.NoSuchMemberException;

import com.depromeet.fairer.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member find(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new NoSuchMemberException("해당하는 멤버를 찾을 수 없습니다."));
    }

    public Member findWithTeam(Long memberId) {
        return memberRepository.findWithTeamByMemberId(memberId).orElseThrow(()->new NoSuchMemberException("해당하는 멤버를 찾을 수 없습니다."));
    }

    public List<Member> getMemberList(Long memberId){
        Team team = findWithTeam(memberId).getTeam();
        return memberRepository.findAllByTeam(team);
    }

    public List<Member> getMemberListByHouseWorkId(Long houseWorkId) {
        return memberRepository.getMemberDtoListByHouseWorkId(houseWorkId);
    }

    public Member updateMember(Long memberId, String memberName, String profilePath, String statusMessage) {
        Member member = this.find(memberId);
        member.update(memberName, profilePath, statusMessage);
        return memberRepository.save(member);
    }
}
