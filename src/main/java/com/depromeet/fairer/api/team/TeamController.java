package com.depromeet.fairer.api.team;

import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.team.request.TeamCreateRequestDto;
import com.depromeet.fairer.dto.team.request.TeamJoinRequestDto;
import com.depromeet.fairer.dto.team.response.TeamCreateResponseDto;
import com.depromeet.fairer.dto.team.response.TeamInviteCodeResponseDto;
import com.depromeet.fairer.dto.team.response.TeamJoinResponseDto;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;


    @PostMapping(value = "")
    public ResponseEntity<TeamCreateResponseDto> createTeam(@RequestMemberId Long memberId, @RequestBody @Valid TeamCreateRequestDto req) {
        Team newTeam = teamService.createTeam(memberId, req.getTeamName());
        return ResponseEntity.ok(TeamCreateResponseDto.from(newTeam));
    }

    @PostMapping(value = "/join")
    public ResponseEntity<TeamJoinResponseDto> joinTeam(@RequestMemberId Long memberId, @RequestBody @Valid TeamJoinRequestDto req) {
        final Team joinedTeam = teamService.joinTeam(memberId, req.getTeamId(), req.getInviteCode());
        return ResponseEntity.ok(TeamJoinResponseDto.from(joinedTeam));
    }

    @GetMapping(value = "/invite-codes")
    public ResponseEntity<TeamInviteCodeResponseDto> viewTeamInviteCode(@RequestMemberId Long memberId) {
        String inviteCode = teamService.viewInviteCode(memberId);
        return ResponseEntity.ok(TeamInviteCodeResponseDto.from(inviteCode));
    }

    // 2022.06.01 정책 아직 수립되지 않았으므로 구현 미룸 (신동빈)
//    @PostMapping(value = "/leave")
//    public ResponseEntity<?> leaveTeam(@RequestMemberId Long memberId) {
//        teamService.leaveTeam(memberId);
//        return ResponseEntity.ok().build();
//    }

}
