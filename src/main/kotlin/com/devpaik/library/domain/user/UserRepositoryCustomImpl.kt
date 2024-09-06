package com.devpaik.library.domain.user

import com.devpaik.library.domain.user.QUser.user
import com.querydsl.jpa.impl.JPAQueryFactory


class UserRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : UserRepositoryCustom {


    /**
     *    select distinct u
     *         from User u left join fetch u.userLoanHistories
     */
    override fun findAllByWithHistories(): List<User> {
        return queryFactory.select(user)
            .distinct()
            .from(user)
            .leftJoin(user.userLoanHistories)
            .fetchJoin()
            .fetch()
    }
}