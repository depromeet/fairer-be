package com.depromeet.fairer.service.rule;

import com.depromeet.fairer.domain.rule.Rule;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.rule.request.RuleRequestDto;
import com.depromeet.fairer.repository.rule.RuleRepository;
import com.depromeet.fairer.service.member.MemberService;
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
    private final RuleRepository ruleRepository;
    private final MemberService memberService;

    @Transactional
    public Rule createRules(Long memberId, String ruleName){
        Team team = memberService.findWithTeam(memberId).getTeam();
        Rule rule = Rule.builder().team(team).ruleName(ruleName).build();
        ruleRepository.save(rule);

        return rule;
    }

    @Transactional
    public List<Rule> getRules(Long memberId){
        return ruleRepository.findAllByTeam(memberService.findWithTeam(memberId).getTeam());
    }

    @Transactional
    public List<Rule> deleteRules(Long memberId, Long ruleId){
        ruleRepository.deleteById(ruleId);
        return ruleRepository.findAllByTeam(memberService.findWithTeam(memberId).getTeam());
    }

    public List<Rule> findAllByTeam(Team team) {
        return ruleRepository.findAllByTeam(team);
    }
}
