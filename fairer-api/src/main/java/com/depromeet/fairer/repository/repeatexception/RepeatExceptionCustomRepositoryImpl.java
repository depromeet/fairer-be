package com.depromeet.fairer.repository.repeatexception;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.housework.QHouseWork;
import com.depromeet.fairer.domain.repeatexception.QRepeatException;
import com.depromeet.fairer.domain.repeatexception.RepeatException;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.depromeet.fairer.domain.housework.QHouseWork.houseWork;
import static com.depromeet.fairer.domain.repeatexception.QRepeatException.repeatException;

@Repository
@RequiredArgsConstructor
public class RepeatExceptionCustomRepositoryImpl implements RepeatExceptionCustomRepository{
    private final JPQLQueryFactory factory;

    @Override
    public void deleteAfterEndDate(Long houseWorkId) {
        factory.delete(repeatException)
                .where(repeatException.in(
                        JPAExpressions
                                .selectFrom(repeatException)
                                .innerJoin(repeatException.houseWork, houseWork)
                                .where(houseWork.houseWorkId.eq(houseWorkId)
                                        .and(repeatException.exceptionDate.after(houseWork.repeatEndDate)))
                ));
    }
}
