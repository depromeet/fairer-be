package com.depromeet.fairer.service.team;

import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.team.request.TeamRuleRequestDto;
import com.depromeet.fairer.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

    private final MemberService memberService;

    @Transactional
    public Team createTeamRules(Long memberId, TeamRuleRequestDto req){
        Team team = memberService.getTeam(memberId);
        team.getRules().add(req.getRuleName());
        return team;
    }

    @Transactional
    public Team getTeamRules(Long memberId){
        Team team = memberService.getTeam(memberId);
        return team;
    }

    @Transactional
    public Team deleteTeamRules(Long memberId, TeamRuleRequestDto req){
        Team team = memberService.getTeam(memberId);
        team.getRules().remove(req.getRuleName());
        return team;
    }
}