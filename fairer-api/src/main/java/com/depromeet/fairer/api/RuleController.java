package com.depromeet.fairer.api;
import com.depromeet.fairer.domain.rule.Rule;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.common.CommonApiResult;
import com.depromeet.fairer.dto.rule.request.RuleRequestDto;
import com.depromeet.fairer.dto.rule.response.RuleResponseDto;
import com.depromeet.fairer.dto.rule.response.RulesResponseDto;
import com.depromeet.fairer.global.exception.BadRequestException;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.member.MemberService;
import com.depromeet.fairer.service.rule.RuleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "rules", description = "규칙 API")
@RequestMapping("/api/rules")
public class RuleController {

    private final RuleService ruleService;
    private final MemberService memberService;

    @Tag(name = "rules")
    @ApiOperation(value = "팀 규칙 생성", notes = "")
    @PostMapping(value = "")
    public ResponseEntity<RulesResponseDto> createTeamRules(@ApiIgnore @RequestMemberId Long memberId,
                                                     @RequestBody @Valid RuleRequestDto req){
        Team team = memberService.findWithTeam(memberId).getTeam();

        // 규칙 조회
        Long count = ruleService.countRules(team);
        if(count >= 10) {
            throw new BadRequestException("규칙은 최대 10개까지 생성할 수 있습니다.");
        }

        // 규칙 생성
        ruleService.createRules(team, req.getRuleName());

        // 반환 객체 생성
        List<Rule> rules = ruleService.findAllByTeam(team);

        List<RuleResponseDto> ruleResponseDtos = rules.stream().map(RuleResponseDto::createRule).collect(Collectors.toList());
        return ok(RulesResponseDto.createRules(team.getTeamId(), ruleResponseDtos));
    }

    @Tag(name = "rules")
    @ApiOperation(value = "팀 규칙 조회", notes = "memberId를 통한 팀 규칙 조회")
    @GetMapping(value = "")
    public ResponseEntity<RulesResponseDto> getTeamRules(@ApiIgnore @RequestMemberId Long memberId){
        List<Rule> rules = ruleService.getRules(memberId);

        List<RuleResponseDto> ruleResponseDtos = new ArrayList<>();
        for(Rule rulee : rules){
            ruleResponseDtos.add(RuleResponseDto.createRule(rulee));
        }

        Long teamId = memberService.findWithTeam(memberId).getTeam().getTeamId();
        return ok(RulesResponseDto.createRules(teamId, ruleResponseDtos));
    }

    @Tag(name = "rules")
    @ApiOperation(value = "팀 규칙 삭제", notes = "ruleId를 통한 규칙 삭제")
    @DeleteMapping(value = "{ruleId}")
    public ResponseEntity<CommonApiResult> deleteTeamRules(@ApiIgnore @RequestMemberId Long memberId,
                                                           @PathVariable Long ruleId){
        List<Rule> rules = ruleService.deleteRules(memberId, ruleId);

        return ResponseEntity.ok(CommonApiResult.createOk("규칙 삭제 완료"));
    }
}
