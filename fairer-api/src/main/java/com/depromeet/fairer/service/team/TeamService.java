package com.depromeet.fairer.service.team;


import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.fcm.FCMMessageTemplate;
import com.depromeet.fairer.dto.fcm.request.FCMMessageRequest;
import com.depromeet.fairer.global.exception.BadRequestException;
import com.depromeet.fairer.global.exception.CannotJoinTeamException;
import com.depromeet.fairer.global.exception.MemberTokenNotFoundException;
import com.depromeet.fairer.global.exception.PermissionDeniedException;
import com.depromeet.fairer.repository.assignment.AssignmentRepository;
import com.depromeet.fairer.repository.housework.HouseWorkRepository;
import com.depromeet.fairer.repository.member.MemberRepository;
import com.depromeet.fairer.repository.team.TeamRepository;
import com.depromeet.fairer.service.fcm.FCMService;
import com.depromeet.fairer.service.member.MemberService;
import com.depromeet.fairer.vo.team.InviteCodeVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final MemberService memberService;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final AssignmentRepository assignmentRepository;
    private final HouseWorkRepository houseWorkRepository;
    private final FCMService fcmService;

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

    public Team joinTeam(Long memberId, String inviteCode) {
        final Member reqMember = memberService.findWithTeam(memberId);

        if (reqMember.getTeam() != null) {
            throw new CannotJoinTeamException();
        }
        final Team team = teamRepository.findWithMembersByInviteCode(inviteCode)
                .orElseThrow(() -> new BadRequestException("해당하는 팀이 존재하지 않습니다."));

        validateInviteCode(team, inviteCode);

        Member member = reqMember.joinTeam(team);

        pushMessageToTeamMember(member, team.getMembers());

        return member.getTeam();
    }

    private void pushMessageToTeamMember(Member member, Set<Member> teamMemberList) {
        for(Member teamMember : teamMemberList) {
            FCMMessageRequest request = new FCMMessageRequest();
            request.setMemberId(teamMember.getMemberId());
            request.setTitle(String.format(FCMMessageTemplate.JOIN_MEMBER_IN_TEAM.getTitle(), member.getMemberName(), member.getTeam().getTeamName()));
            request.setBody(FCMMessageTemplate.JOIN_MEMBER_IN_TEAM.getBody());
            fcmService.sendMessage(request);
        }
    }

    private void validateInviteCode(Team team, String reqInviteCode) {

        // 초대 코드 및 유효기간 검증
        if (!team.getInviteCode().equals(reqInviteCode) ||
                team.isExpiredInviteCode(LocalDateTime.now())) {
            throw new BadRequestException("초대 코드를 다시 확인해주세요.");
        }
    }

    public InviteCodeVo viewInviteCode(Long memberId) {
        final Team reqTeam = memberService.findWithTeam(memberId).getTeam();

        if (reqTeam == null) {
            throw new BadRequestException("속한 팀이 없어 초대 코드를 조회할 수 없어요.");
        }

        // 초대 코드 만료 시 재생성
        if (reqTeam.isExpiredInviteCode(LocalDateTime.now())) {
            reqTeam.createNewInviteCode();
        }
        return new InviteCodeVo(reqTeam.getInviteCode(), reqTeam.getInviteCodeExpirationDateTime(), reqTeam.getTeamName());
    }

    public Team updateTeam(Long memberId, String teamName) {
        final Team reqTeam = memberService.findWithTeam(memberId).getTeam();

        if (reqTeam == null) {
            throw new BadRequestException("소속된 팀이 없습니다.");
        }

        if (teamName != null) {
            reqTeam.updateTeamName(teamName);
        }

        return reqTeam;
    }

    public void leaveTeam(Long memberId) {
        final Member member = memberService.findWithTeam(memberId);

        if (!member.hasTeam()) {
            throw new BadRequestException("소속된 팀이 없습니다.");
        }

        List<Assignment> assignmentList = assignmentRepository.findAllByMember(member);
        assignmentRepository.deleteAll(assignmentList);

        for(Assignment assignment : assignmentList) {
            int count = assignmentRepository.findAllByHouseWorkAndMemberNotIn(assignment.getHouseWork(), List.of(member)).size();
            if(count == 0) {
                houseWorkRepository.delete(assignment.getHouseWork());
            }
        }

        Team team = member.getTeam();
        team.getMembers().remove(member);
        member.setTeam(null);
        memberRepository.save(member);
    }

    public Team getTeam(Long memberId) {
        final Team team = memberRepository.findWithTeamByMemberId(memberId)
                .orElseThrow(() -> new MemberTokenNotFoundException("해당 맴버가 존재하지 않습니다"))
                .getTeam();
        if (team == null) {
            throw new BadRequestException("가입된 팀이 없습니다.");
        }
        return team;
    }

    public void checkJoinSameTeam(Long teamMemberId, Long memberId) {
        Member teamMember = memberService.find(teamMemberId);
        Member member = memberService.find(memberId);

        if (!Objects.equals(member.getTeam(), teamMember.getTeam())) {
            throw new PermissionDeniedException("해당 멤버의 정보를 가져올 수 없습니다.");
        }
    }
}