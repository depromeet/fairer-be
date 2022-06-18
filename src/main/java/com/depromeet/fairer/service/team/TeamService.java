package com.depromeet.fairer.service.team;


import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.global.exception.BadRequestException;
import com.depromeet.fairer.global.exception.CannotJoinTeamException;
import com.depromeet.fairer.repository.member.MemberRepository;
import com.depromeet.fairer.repository.team.TeamRepository;
import com.depromeet.fairer.service.member.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final MemberService memberService;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    public Team createTeam(Long memberId, String teamName) {
        final Member reqMember = memberService.findWithTeam(memberId);

        if (reqMember.getTeam() != null) {
            throw new CannotJoinTeamException();
        }

        final Team newTeam = Team.builder()
                .member(reqMember)
                .teamName(teamName)
                .build();
        return teamRepository.save(newTeam);
    }

    public Team joinTeam(Long memberId, Long teamId, String inviteCode) {
        final Member reqMember = memberService.findWithTeam(memberId);

        if (reqMember.getTeam() != null) {
            throw new CannotJoinTeamException();
        }
        final Team team = teamRepository.findWithMembersByTeamId(teamId)
                .orElseThrow(() -> new BadRequestException("해당하는 팀이 존재하지 않습니다."));

        validateInviteCode(team, inviteCode);

        reqMember.joinTeam(team);
        return team;
    }

    private void validateInviteCode(Team team, String reqInviteCode) {

        // 초대 코드 및 유효기간 검증
        if (!team.getInviteCode().equals(reqInviteCode) ||
                team.isExpiredInviteCode(LocalDateTime.now())) {
            throw new BadRequestException("초대 코드를 다시 확인해주세요.");
        }
    }

    public String viewInviteCode(Long memberId) {
        final Team reqTeam = memberService.findWithTeam(memberId).getTeam();

        if (reqTeam == null) {
            throw new BadRequestException("속한 팀이 없어 초대 코드를 조회할 수 없어요.");
        }

        // 초대 코드 만료 시 재생성
        if (reqTeam.isExpiredInviteCode(LocalDateTime.now())) {
            reqTeam.createNewInviteCode();
        }
        return reqTeam.getInviteCode();
    }

    public Team updateTeam(Long memberId, String teamName) {
        final Team reqTeam = memberService.findWithTeam(memberId).getTeam();

        if (reqTeam == null) {
            throw new BadRequestException("속한 팀이 없습니다.");
        }

        if (teamName != null) {
            reqTeam.updateTeamName(teamName);
        }

        return reqTeam;
    }

    // 2022.06.01 정책 아직 수립되지 않았으므로 구현 미룸 (신동빈)
//    public void leaveTeam(Long memberId) {
//        final Member reqMember = memberService.findWithTeam(memberId);
//        final Team team = reqMember.getTeam();
//        reqMember.setTeam(null);
//        memberRepository.save(reqMember);
//
//        log.info("names: {}", team.getMembers().stream().map(Member::getMemberName).collect(Collectors.toList()));

//        foundTeam.getMembers().remove(reqMember);
//        teamRepository.save(foundTeam);
//    }

    public Team getTeam(Long memberId) {
        return memberRepository.findById(memberId).get().getTeam();
    }
}