package com.depromeet.fairer.domain.group;

import com.depromeet.fairer.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "fairer_group")
@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long groupId;

    @OneToMany(mappedBy = "group")
    private List<Member> members;
}
