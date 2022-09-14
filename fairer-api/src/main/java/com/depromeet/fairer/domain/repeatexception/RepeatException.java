package com.depromeet.fairer.domain.repeatexception;

import com.depromeet.fairer.domain.housework.HouseWork;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "repeat_exception")
@Builder
@NoArgsConstructor @AllArgsConstructor
public class RepeatException {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "repeat_exception_id", columnDefinition = "BIGINT")
    private Long repeatExceptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "housework_id")
    private HouseWork houseWork;

    @Column(name = "exception_date", columnDefinition = "DATE", nullable = false)
    private LocalDate exceptionDate;

    public static RepeatException create(HouseWork houseWork, LocalDate exceptionDate) {
        return RepeatException.builder()
                .exceptionDate(exceptionDate)
                .houseWork(houseWork)
                .build();
    }
}
