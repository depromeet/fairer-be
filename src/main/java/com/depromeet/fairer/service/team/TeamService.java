package com.depromeet.fairer.service.team;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

    private final MemberRepository memberRepository;

    public Team getTeam(Long memberId) {
        return memberRepository.findById(memberId).get().getTeam();
    }

}