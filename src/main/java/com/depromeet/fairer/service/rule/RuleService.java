package com.depromeet.fairer.service.rule;

import com.depromeet.fairer.domain.rule.Rule;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.rule.request.RuleRequestDto;
import com.depromeet.fairer.repository.rule.RuleRepository;
import com.depromeet.fairer.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuleService {
    private final TeamService teamService;
    private final RuleRepository ruleRepository;

    @Transactional
    public Rule createRules(Long memberId, RuleRequestDto req){
        Team team = teamService.getTeam(memberId);
        Rule rule = Rule.builder().team(team).ruleName(req.getRuleName()).build();
        ruleRepository.save(rule);

        return rule;
    }

    @Transactional
    public List<Rule> getRules(Long memberId){
        return ruleRepository.findAllByTeam(teamService.getTeam(memberId));
    }

    @Transactional
    public List<Rule> deleteRules(Long memberId, Long ruleId){
        ruleRepository.deleteById(ruleId);
        return ruleRepository.findAllByTeam(teamService.getTeam(memberId));
    }
}
