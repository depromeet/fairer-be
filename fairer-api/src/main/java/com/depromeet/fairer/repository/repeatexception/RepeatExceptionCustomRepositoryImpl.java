package com.depromeet.fairer.repository.repeatexception;

import com.depromeet.fairer.domain.repeatexception.QRepeatException;
import com.depromeet.fairer.domain.repeatexception.RepeatException;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
                .where(e1.eq(
                        JPAExpressions
                                .select(repeatException)
                                .from(repeatException)
                                .innerJoin(repeatException.houseWork, houseWork)
                                .on(repeatException.houseWork.houseWorkId.eq(houseWorkId)
                                        .and(repeatException.exceptionDate.after(repeatException.houseWork.repeatEndDate))))
                ).execute();*/
        final Optional<RepeatException> foundRepeatException = factory.selectFrom(QRepeatException.repeatException)
                .where(QRepeatException.repeatException.eq(
                        JPAExpressions
                                .selectFrom(QRepeatException.repeatException)
                                .innerJoin(QRepeatException.repeatException.houseWork, houseWork)
                                .on(QRepeatException.repeatException.houseWork.houseWorkId.eq(houseWorkId)
                                        .and(QRepeatException.repeatException.exceptionDate.after(QRepeatException.repeatException.houseWork.repeatEndDate))))).fetch().stream().findFirst();

        final QRepeatException target = new QRepeatException("target");
        if (foundRepeatException.isPresent()) {
            factory.delete(target)
                    .where(target.eq(foundRepeatException.get()))
                    .execute();
        }
    }
}
