package com.depromeet.fairer.api.team;

import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.team.request.TeamRuleRequestDto;
import com.depromeet.fairer.dto.team.response.TeamRuleResponseDto;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping(value = "/rules")
    public ResponseEntity<TeamRuleResponseDto> createTeamRules(@RequestMemberId Long memberId,
                                                               @RequestBody @Valid TeamRuleRequestDto req){
        // 규칙 생성
        Team team = teamService.createTeamRules(memberId, req);

        // 반환 객체 생성
        TeamRuleResponseDto teamRuleResponseDto = TeamRuleResponseDto.builder()
                .teamId(team.getTeamId())
                .rules(team.getRules())
                .build();

        return ok(teamRuleResponseDto);
    }

    @GetMapping(value = "/rules")
    public ResponseEntity<TeamRuleResponseDto> getTeamRules(@RequestMemberId Long memberId){
        Team team = teamService.getTeamRules(memberId);

        TeamRuleResponseDto teamRuleResponseDto = TeamRuleResponseDto.builder()
                .teamId(team.getTeamId())
                .rules(team.getRules())
                .build();

        return ok(teamRuleResponseDto);
    }

    @DeleteMapping(value = "/rules")
    public ResponseEntity<TeamRuleResponseDto> deleteTeamRules(@RequestMemberId Long memberId,
                                                               @RequestBody @Valid TeamRuleRequestDto req){
        Team team = teamService.deleteTeamRules(memberId, req);

        TeamRuleResponseDto teamRuleResponseDto = TeamRuleResponseDto.builder()
                .teamId(team.getTeamId())
                .rules(team.getRules())
                .build();

        return ok(teamRuleResponseDto);
    }
}