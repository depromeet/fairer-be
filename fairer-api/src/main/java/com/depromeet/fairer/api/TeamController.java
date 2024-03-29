package com.depromeet.fairer.api;

import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.team.request.TeamCreateRequestDto;
import com.depromeet.fairer.dto.team.request.TeamJoinRequestDto;
import com.depromeet.fairer.dto.team.request.TeamUpdateRequestDto;
import com.depromeet.fairer.dto.team.response.*;
import com.depromeet.fairer.global.exception.BadRequestException;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.team.TeamService;
import com.depromeet.fairer.vo.team.InviteCodeVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Slf4j
@Tag(name = "teams", description = "팀 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    @Tag(name = "teams")
    @ApiOperation(value = "팀 생성", notes = "팀 생성 후 5분간 유효한 12글자 초대 코드 반환<br/><br/>" +
            "이미 속한 팀 있을 경우 예외 발생")
    @PostMapping(value = "")
    public ResponseEntity<TeamCreateResponseDto> createTeam(@ApiIgnore @RequestMemberId Long memberId, @RequestBody @Valid TeamCreateRequestDto req) {
        Team newTeam = teamService.createTeam(memberId, req.getTeamName());
        return ResponseEntity.ok(TeamCreateResponseDto.from(newTeam));
    }

    @Tag(name = "teams")
    @ApiOperation(value = "팀 참가", notes = "기존 팀에 초대코드를 이용하여 참가<br/><br/>" +
            "예외 상황<br/>" +
            "- 이미 속한 팀이 있을 때<br/>" +
            "- 참가 요청한 팀 id에 해당하는 팀이 존재하지 않을 때<br/>" +
            "- 요청한 초대 코드가 일치하지 않을 때<br/>" +
            "- 요청한 초대코드의 만료 시간(5분)이 지났을 때")
    @PostMapping(value = "/join")
    public ResponseEntity<TeamJoinResponseDto> joinTeam(@ApiIgnore @RequestMemberId Long memberId, @RequestBody @Valid TeamJoinRequestDto req) {
        final Team joinedTeam = teamService.joinTeam(memberId, req.getInviteCode());
        return ResponseEntity.ok(TeamJoinResponseDto.from(joinedTeam));
    }

    @Tag(name = "teams")
    @ApiOperation(value = "팀 초대(초대 코드 보기)", notes = "자신이 속한 팀의 초대 코드 조회<br/>" +
            "초대 코드 만료됐을 경우 새로 생성하여 반환<br/><br/>" +
            "속한 팀이 없을 경우 예외 발생")
    @GetMapping(value = "/invite-codes")
    public ResponseEntity<TeamInviteCodeResponseDto> viewTeamInviteCode(@ApiIgnore @RequestMemberId Long memberId) {
        InviteCodeVo inviteCodeVo = teamService.viewInviteCode(memberId);
        return ResponseEntity.ok(TeamInviteCodeResponseDto.from(inviteCodeVo));
    }

    @Tag(name = "teams")
    @ApiOperation(value = "팀 업데이트", notes = "팀 이름 업데이트 - 필요시 필드 추가 예정<br/><br/>" +
            "속한 팀이 없을 경우 예외 발생")
    @PatchMapping(value = "")
    public ResponseEntity<TeamUpdateResponseDto> updateTeam(@ApiIgnore @RequestMemberId Long memberId, @RequestBody TeamUpdateRequestDto requestDto) {
        Team updatedTeam = teamService.updateTeam(memberId, requestDto.getTeamName());
        return ResponseEntity.ok(TeamUpdateResponseDto.from(updatedTeam));
    }

    @Tag(name = "teams")
    @ApiOperation(value = "팀 나가기", notes = "팀 나가기 - 소속된 팀에서 나가기 처리 <br/>속한 팀이 없을 경우 400 에러 반환")
    @PostMapping(value = "/leave")
    public ResponseEntity<?> leaveTeam(@ApiIgnore @RequestMemberId Long memberId) {
        teamService.leaveTeam(memberId);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "teams")
    @ApiOperation(value = "팀 정보 조회 API", notes = "팀 정보 조회(팀 이름, 멤버 정보 등)")
    @GetMapping("/my")
    public ResponseEntity<TeamInfoResponseDto> viewMyTeamInfo(@ApiIgnore @RequestMemberId Long memberId) {
        Team team = teamService.getTeam(memberId);
        if (team == null) {
            throw new BadRequestException("그룹에 소속되어있지 않아 정보를 조회할 수 없습니다.");
        }

        return ResponseEntity.ok(TeamInfoResponseDto.from(teamService.getTeam(memberId)));
    }
}