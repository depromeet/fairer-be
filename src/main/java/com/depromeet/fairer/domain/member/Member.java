package com.depromeet.fairer.domain.member;

import com.depromeet.fairer.domain.base.BaseTimeEntity;
import com.depromeet.fairer.dto.member.oauth.OAuthAttributes;
import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.memberToken.MemberToken;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.domain.member.constant.SocialType;
import com.depromeet.fairer.global.exception.CannotJoinTeamException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(name = "Member.withMyTeamMembers", attributeNodes = {
        @NamedAttributeNode(value = "team", subgraph = "team")
    },
    subgraphs = @NamedSubgraph(name = "team", attributeNodes = {
        @NamedAttributeNode("members")
    })
)
@Entity
@Table(name = "member")
@Getter
@Setter
@ToString(exclude = {"team", "assignments"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long memberId;

    @Column(name = "email", columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    private String email;

    @Column(name = "profile_path", columnDefinition = "VARCHAR(200) default ''", nullable = false)
    private String profilePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", columnDefinition = "VARCHAR(50)", nullable = false)
    SocialType socialType;

    @Column(name = "member_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String memberName;

    @JsonIgnore
    @Column(name = "password", columnDefinition = "VARCHAR(300)", nullable = false)
    private String password;

    @Column(name = "status_message", columnDefinition = "VARCHAR(40)")
    private String statusMessage;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignment> assignments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "fcm_token", columnDefinition = "VARCHAR(200)")
    private String fcmToken;

    @Column(name = "fcm_token_date", columnDefinition = "DATETIME")
    private LocalDateTime fcmTokenDate;

    /**
     * TODO 닉네임 동의 안했을 때 처리 (입력한 닉네임으로 변경)
     *
     * @param socialUserInfo
     * @return
     */
    public static Member create(OAuthAttributes socialUserInfo) {
        return Member.builder()
                .email(socialUserInfo.getEmail())
                .socialType(socialUserInfo.getSocialType())
                .password(socialUserInfo.getPassword())
                .assignments(new ArrayList<>())
                .memberName("") // 회원가입 할때는 빈값으로 세팅, 이후 멤버 업데이트 api 로 변경
                .profilePath("")
                .statusMessage("")
                .build();
    }

    public Member joinTeam(Team team) {
        if (6 < team.getMembers().size()) {
            throw new CannotJoinTeamException("해당 팀에 구성원이 가득차 참여할 수 없습니다.");
        }
        if (this.team != null) {
            this.team.getMembers().remove(this);
        }
        this.team = team;
        team.getMembers().add(this);
        return this;
    }

    public boolean hasTeam() {
        return this.team != null;
    }

    public void update(String memberName, String profilePath, String statusMessage) {
        this.memberName = memberName;
        this.profilePath = profilePath;
        this.statusMessage = statusMessage;
    }
}
