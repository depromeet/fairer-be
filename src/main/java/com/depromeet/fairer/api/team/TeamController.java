package com.depromeet.fairer.api.team;

import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.team.request.TeamCreateRequestDto;
import com.depromeet.fairer.dto.team.request.TeamJoinRequestDto;
import com.depromeet.fairer.dto.team.request.TeamUpdateRequestDto;
import com.depromeet.fairer.dto.team.response.*;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.team.TeamService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;


    @ApiOperation(value = "팀 생성", notes = "팀 생성 후 5분간 유효한 12글자 초대 코드 반환<br/><br/>" +
            "이미 속한 팀 있을 경우 예외 발생")
    @PostMapping(value = "")
    public ResponseEntity<TeamCreateResponseDto> createTeam(@RequestMemberId Long memberId, @RequestBody @Valid TeamCreateRequestDto req) {
        Team newTeam = teamService.createTeam(memberId, req.getTeamName());
        return ResponseEntity.ok(TeamCreateResponseDto.from(newTeam));
    }

    @ApiOperation(value = "팀 참가", notes = "기존 팀에 초대코드를 이용하여 참가<br/><br/>" +
            "예외 상황<br/>" +
            "- 이미 속한 팀이 있을 때<br/>" +
            "- 참가 요청한 팀 id에 해당하는 팀이 존재하지 않을 때<br/>" +
            "- 요청한 초대 코드가 일치하지 않을 때<br/>" +
            "- 요청한 초대코드의 만료 시간(5분)이 지났을 때")
    @PostMapping(value = "/join")
    public ResponseEntity<TeamJoinResponseDto> joinTeam(@RequestMemberId Long memberId, @RequestBody @Valid TeamJoinRequestDto req) {
        final Team joinedTeam = teamService.joinTeam(memberId, req.getTeamId(), req.getInviteCode());
        return ResponseEntity.ok(TeamJoinResponseDto.from(joinedTeam));
    }

    @ApiOperation(value = "팀 초대(초대 코드 보기)", notes = "자신이 속한 팀의 초대 코드 조회<br/>" +
            "초대 코드 만료됐을 경우 새로 생성하여 반환<br/><br/>" +
            "속한 팀이 없을 경우 예외 발생")
    @GetMapping(value = "/invite-codes")
    public ResponseEntity<TeamInviteCodeResponseDto> viewTeamInviteCode(@RequestMemberId Long memberId) {
        String inviteCode = teamService.viewInviteCode(memberId);
        return ResponseEntity.ok(TeamInviteCodeResponseDto.from(inviteCode));
    }

    @ApiOperation(value = "팀 업데이트", notes = "팀 이름 업데이트 - 필요시 필드 추가 예정<br/><br/>" +
            "속한 팀이 없을 경우 예외 발생")
    @PatchMapping(value = "")
    public ResponseEntity<TeamUpdateResponseDto> updateTeam(@RequestMemberId Long memberId, @RequestBody TeamUpdateRequestDto requestDto) {
        Team updatedTeam = teamService.updateTeam(memberId, requestDto.getTeamName());
        return ResponseEntity.ok(TeamUpdateResponseDto.from(updatedTeam));
    }

    // 2022.06.01 정책 아직 수립되지 않았으므로 구현 미룸 (신동빈)
//    @PostMapping(value = "/leave")
//    public ResponseEntity<?> leaveTeam(@RequestMemberId Long memberId) {
//        teamService.leaveTeam(memberId);
//        return ResponseEntity.ok().build();
//    }

    @ApiOperation(value = "팀 멤버 정보 조회", notes = "팀에 소속된 멤버 목록 조회")
    @GetMapping("/members")
    public ResponseEntity<Map<String, Object>> viewTeamMembers(@RequestMemberId Long memberId) {
        Map<String, Object> result = new HashMap<>();
        Set<Member> teamMembers = teamService.getTeamMembers(memberId);
        List<TeamMemberResponseDto> membersDto = teamMembers.stream().map(TeamMemberResponseDto::from).collect(Collectors.toList());
        result.put("members", membersDto);

        return ResponseEntity.ok(result);
    }

}
