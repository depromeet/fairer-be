package com.depromeet.fairer.domain.member;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.group.Group;
import com.depromeet.fairer.domain.member.constant.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="fairer_member")
@Getter
@ToString
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
    @JoinColumn(name = "group_id")
    private Group group;
}
