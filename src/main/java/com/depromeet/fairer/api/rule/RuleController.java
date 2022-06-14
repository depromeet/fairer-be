package com.depromeet.fairer.api.rule;
import com.depromeet.fairer.domain.rule.Rule;
import com.depromeet.fairer.dto.rule.request.RuleRequestDto;
import com.depromeet.fairer.dto.rule.response.RuleResponseDto;
import com.depromeet.fairer.dto.rule.response.RulesResponseDto;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.repository.rule.RuleRepository;
import com.depromeet.fairer.service.rule.RuleService;
import com.depromeet.fairer.service.team.TeamService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rules")
public class RuleController {

    private final RuleService ruleService;
    private final RuleRepository ruleRepository;
    private final TeamService teamService;

    @ApiOperation(value = "팀 규칙 생성", notes = "")
    @PostMapping(value = "")
    public ResponseEntity<RulesResponseDto> createTeamRules(@RequestMemberId Long memberId,
                                                     @RequestBody @Valid RuleRequestDto req){
        // 규칙 생성
        Rule rule = ruleService.createRules(memberId, req.getRuleName());

        // 반환 객체 생성
        List<Rule> rules = ruleRepository.findAllByTeam(rule.getTeam());

        List<RuleResponseDto> ruleResponseDtos = new ArrayList<>();
        for(Rule rulee : rules){
            ruleResponseDtos.add(RuleResponseDto.createRule(rulee));
        }

        return ok(RulesResponseDto.createRules(teamService.getTeam(memberId).getTeamId(), ruleResponseDtos));
    }

    @ApiOperation(value = "팀 규칙 조회", notes = "memberId를 통한 팀 규칙 조회")
    @GetMapping(value = "")
    public ResponseEntity<RulesResponseDto> getTeamRules(@RequestMemberId Long memberId){
        List<Rule> rules = ruleService.getRules(memberId);

        List<RuleResponseDto> ruleResponseDtos = new ArrayList<>();
        for(Rule rulee : rules){
            ruleResponseDtos.add(RuleResponseDto.createRule(rulee));
        }

        return ok(RulesResponseDto.createRules(teamService.getTeam(memberId).getTeamId(), ruleResponseDtos));
    }

    @ApiOperation(value = "팀 규칙 삭제", notes = "ruleId를 통한 규칙 삭제")
    @DeleteMapping(value = "{ruleId}")
    public ResponseEntity<RulesResponseDto> deleteTeamRules(@RequestMemberId Long memberId,
                                                               @PathVariable Long ruleId){
        List<Rule> rules = ruleService.deleteRules(memberId, ruleId);

        List<RuleResponseDto> ruleResponseDtos = new ArrayList<>();
        for(Rule rulee : rules){
            ruleResponseDtos.add(RuleResponseDto.createRule(rulee));
        }

        return ok(RulesResponseDto.createRules(teamService.getTeam(memberId).getTeamId(), ruleResponseDtos));
    }
}
