package com.depromeet.fairer.service.rule;

import com.depromeet.fairer.domain.rule.Rule;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.rule.request.RuleRequestDto;
import com.depromeet.fairer.global.exception.BadRequestException;
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
    public Rule createRules(Team team, String ruleName){
        Rule rule = Rule.builder().team(team).ruleName(ruleName).build();
        return ruleRepository.save(rule);
    }

    @Transactional
    public List<Rule> getRules(Long memberId){
        return ruleRepository.findAllByTeam(memberService.findWithTeam(memberId).getTeam());
    }

    @Transactional
    public void deleteRules(Long memberId, Long ruleId){
        Team team = memberService.findWithTeam(memberId).getTeam();
        Rule rule = ruleRepository.findById(ruleId).orElseThrow(() -> new BadRequestException("해당하는 rule id값이 존재하지 않습니다."));
        if(!team.equals(rule.getTeam())) {
            new BadRequestException("팀과 규칙이 매칭되지 않습니다.");
        }
        ruleRepository.delete(rule);
    }

    public List<Rule> findAllByTeam(Team team) {
        return ruleRepository.findAllByTeam(team);
    }

    public Long countRules(Team team) {
        return ruleRepository.countByTeam(team);
    }

}
