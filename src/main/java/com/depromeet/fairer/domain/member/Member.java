package com.depromeet.fairer.domain.member;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.group.Group;
import com.depromeet.fairer.domain.member.constant.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="member")
@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberId;

    @Column(unique = true, nullable = false)
    private String email;

    private String profilePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    SocialType socialType;

    @Column(nullable = false)
    private String memberName;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Assignment> assignments;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
