package com.depromeet.fairer.api.team;

import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.team.request.TeamCreateRequestDto;
import com.depromeet.fairer.dto.team.response.TeamResponseDto;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;


    @PostMapping(value = "/create")
    public ResponseEntity<TeamResponseDto> createTeam(@RequestMemberId Long memberId, @RequestBody @Valid TeamCreateRequestDto req) {
        Team newTeam = teamService.createTeam(memberId, req.getTeamName());
        return ResponseEntity.ok(TeamResponseDto.from(newTeam));
    }

    // 2022.06.01 정책 아직 수립되지 않았으므로 구현 미룸 (신동빈)
//    @PostMapping(value = "/join")
//    public ResponseEntity<TeamResponseDto> joinTeam(@RequestMemberId Long memberId, @RequestBody @Valid TeamJoinRequestDto req) {
//        final Team joinedTeam = teamService.joinTeam(memberId, req.getTeamId());
//        return ResponseEntity.ok(TeamResponseDto.from(joinedTeam));
//    }

    // 2022.06.01 정책 아직 수립되지 않았으므로 구현 미룸 (신동빈)
//    @PostMapping(value = "/leave")
//    public ResponseEntity<?> leaveTeam(@RequestMemberId Long memberId) {
//        teamService.leaveTeam(memberId);
//        return ResponseEntity.ok().build();
//    }

}
