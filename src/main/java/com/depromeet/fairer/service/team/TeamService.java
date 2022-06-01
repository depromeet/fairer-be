package com.depromeet.fairer.service.team;

import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.team.request.TeamCreateRequestDto;
import com.depromeet.fairer.global.exception.CannotCreateException;
import com.depromeet.fairer.repository.member.MemberRepository;
import com.depromeet.fairer.repository.team.TeamRepository;
import com.depromeet.fairer.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
            throw new CannotCreateException("이미 팀에 소속되어 있는 회원입니다.");
        }

        final Team newTeam = Team.builder()
                .member(reqMember)
                .teamName(teamName)
                .build();
        return teamRepository.save(newTeam);
    }

    // 2022.06.01 정책 아직 수립되지 않았으므로 구현 미룸 (신동빈)
//    public Team joinTeam(Long memberId, Long teamId) {
//        final Member reqMember = memberService.find(memberId);
//
//        // 아무나 팀에 참여할 수 있는 것인가?
//        final Team foundTeam = findTeamWithMembersById(teamId);
//        reqMember.joinTeam(foundTeam);
//        return teamRepository.save(foundTeam);
//    }

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

//    private Team findTeamWithMembersById(Long teamId) {
//        return teamRepository.findWithMembersByTeamId(teamId).orElseThrow(() -> {
//                    throw new BadRequestException("해당하는 팀을 찾을 수 없습니다.");
//        });
//    }
//
//    private Team findTeamById(Long teamId) {
//        return teamRepository.findById(teamId).orElseThrow(() -> {
//            throw new BadRequestException("해당하는 팀을 찾을 수 없습니다.");
//        });
//    }
}
