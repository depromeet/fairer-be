package com.depromeet.fairer.domain.member;

import com.depromeet.fairer.dto.member.oauth.OAuthAttributes;
import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.memberToken.MemberToken;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.domain.member.constant.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="member")
@Getter
@ToString(exclude = {"memberToken", "team", "assignments"})
@Builder
@EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long memberId;

    @Column(name = "email", columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    private String email;

    @Column(name = "profile_path", columnDefinition = "VARCHAR(50)")
    private String profilePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", columnDefinition = "VARCHAR(50)", nullable = false)
    SocialType socialType;

    @Column(name = "member_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String memberName;

    @JsonIgnore
    @Column(name = "password", columnDefinition = "VARCHAR(300)", nullable = false)
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Assignment> assignments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberToken memberToken;

    /**
     * TODO 닉네임 동의 안했을 때 처리 (입력한 닉네임으로 변경)
     * @param socialUserInfo
     * @return
     */
    public static Member create(OAuthAttributes socialUserInfo) {
        return Member.builder()
                .memberName(socialUserInfo.getName())
                .email(socialUserInfo.getEmail())
                .socialType(socialUserInfo.getSocialType())
                .password(socialUserInfo.getPassword())
                .assignments(new ArrayList<>())
                .build();
    }

    public void updateMemberToken(MemberToken memberToken) {
        this.memberToken = memberToken;
    }
}
