package com.depromeet.fairer.domain.member;

import com.depromeet.fairer.domain.member.constant.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="member")
@Getter
@ToString
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberId;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    SocialType socialType;

    @Column(nullable = false)
    private String memberName;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

}
