package com.depromeet.fairer.domain.housework;

import com.depromeet.fairer.domain.assignment.Assignment;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "housework")
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
public class Housework {

    @Id
    @Column(name = "housework_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long houseworkId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Space space;

    private String houseworkName;

    private LocalDateTime scheduledDateTime;

    private LocalDateTime successDateTime;

    private boolean success;

    @OneToMany(mappedBy = "work")
    private List<Assignment> assignments;
}
