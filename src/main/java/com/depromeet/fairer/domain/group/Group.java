package com.depromeet.fairer.domain.group;

import com.depromeet.fairer.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "group")
@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
public class Group {

    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long groupId;

    @OneToMany(mappedBy = "group")
    private List<Member> members;
}
