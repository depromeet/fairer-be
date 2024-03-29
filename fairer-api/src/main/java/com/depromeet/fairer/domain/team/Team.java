package com.depromeet.fairer.domain.team;

import com.depromeet.fairer.domain.base.BaseTimeEntity;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.team.constant.TeamConstant;
import com.depromeet.fairer.domain.rule.Rule;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "team")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long teamId;

    @NotNull
    @Column(name = "team_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String teamName;

    private String inviteCode;

    private LocalDateTime inviteCodeCreatedAt;

    @OneToMany(mappedBy = "team")
    private Set<Member> members;

    @OneToMany(mappedBy = "team")
    private List<HouseWork> houseWorks;

    @OneToMany(mappedBy = "team")
    private List<Rule> rules;

    @Builder
    public Team(Member member, String teamName) {
        this.teamName = teamName;
        this.rules = new ArrayList<>();
        this.houseWorks = new ArrayList<>();
        if (this.members == null) {
            members = new HashSet<>();
        }

        createNewInviteCode();

        member.joinTeam(this);
    }

    public void createNewInviteCode() {
        inviteCode = RandomStringUtils.random(12, true, true);
        inviteCodeCreatedAt = LocalDateTime.now();
    }

    public void addMember(Member member) {
        member.joinTeam(this);
    }


    public Boolean isExpiredInviteCode(LocalDateTime now) {
        return now.isAfter(getInviteCodeExpirationDateTime());
    }

    public LocalDateTime getInviteCodeExpirationDateTime() {
        return inviteCodeCreatedAt.plusHours(TeamConstant.INVITE_CODE_EXPIRATION_TIME);
    }

    public void updateTeamName(String teamName) {
        this.teamName = teamName;
    }
}