package com.depromeet.fairer.repository.repeatexception;

import com.depromeet.fairer.domain.repeatexception.QRepeatException;
import com.depromeet.fairer.domain.repeatexception.RepeatException;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.depromeet.fairer.domain.housework.QHouseWork.houseWork;
import static com.depromeet.fairer.domain.repeatexception.QRepeatException.repeatException;

@Repository
@RequiredArgsConstructor
public class RepeatExceptionCustomRepositoryImpl implements RepeatExceptionCustomRepository {
    private final JPQLQueryFactory factory;

    @Override
    public void deleteAfterStandardDate(Long houseWorkId, LocalDate deleteStandardDate) {
        final QRepeatException qRepeatException = new QRepeatException("e1");
        factory.delete(qRepeatException)
                .where(qRepeatException.houseWork.houseWorkId.eq(houseWorkId)
                        .and(qRepeatException.exceptionDate.goe(deleteStandardDate)))
                .execute();
    }
}
