package com.depromeet.fairer.repository.repeatexception;

import com.depromeet.fairer.domain.repeatexception.QRepeatException;
import com.depromeet.fairer.domain.repeatexception.RepeatException;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.depromeet.fairer.domain.housework.QHouseWork.houseWork;
import static com.depromeet.fairer.domain.repeatexception.QRepeatException.repeatException;

@Repository
@RequiredArgsConstructor
public class RepeatExceptionCustomRepositoryImpl implements RepeatExceptionCustomRepository {
    private final JPQLQueryFactory factory;

    @Override
    public void deleteAfterEndDate(Long houseWorkId) {
        /*final QRepeatException e1 = new QRepeatException("e1");
        factory.delete(e1)
                .where(e1.in(
                        JPAExpressions
                                .select(repeatException)
                                .from(repeatException)
                                .innerJoin(repeatException.houseWork, houseWork)
                                .on(repeatException.houseWork.houseWorkId.eq(houseWorkId)
                                        .and(repeatException.exceptionDate.after(repeatException.houseWork.repeatEndDate))))
                ).execute();*/
        final List<RepeatException> repeatExceptions = factory.selectFrom(repeatException)
                .where(repeatException.in(
                        JPAExpressions
                                .selectFrom(repeatException)
                                .innerJoin(repeatException.houseWork, houseWork)
                                .on(repeatException.houseWork.houseWorkId.eq(houseWorkId)
                                        .and(repeatException.exceptionDate.after(repeatException.houseWork.repeatEndDate))))).fetch();

        final QRepeatException target = new QRepeatException("target");
        if (repeatExceptions != null) {
            for (RepeatException repeatException : repeatExceptions) {
                factory.delete(target)
                        .where(target.eq(repeatException))
                        .execute();
            }
        }
    }
}
