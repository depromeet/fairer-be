package com.depromeet.fairer.repository.alarm;

import com.depromeet.fairer.domain.alarm.Alarm;
import com.depromeet.fairer.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Alarm findByMember(Member member);
}
