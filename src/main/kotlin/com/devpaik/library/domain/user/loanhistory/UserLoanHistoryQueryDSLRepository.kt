package com.devpaik.library.domain.user.loanhistory

import com.devpaik.library.domain.user.loanhistory.QUserLoanHistory.userLoanHistory
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class UserLoanHistoryQueryDSLRepository(
    private val queryFactory: JPAQueryFactory
) {
    fun findByBookNameAndStatus(bookName: String, status: UserLoanStatus? = null): UserLoanHistory? {
        return queryFactory.select(userLoanHistory)
            .from(userLoanHistory)
            .where(userLoanHistory.bookName.eq(bookName),
                status?.let { userLoanHistory.status.eq(status) }
            )
            .limit(1)
            .fetchOne()
    }

    fun countAllByStatus(status: UserLoanStatus): Long {
        return queryFactory.select(userLoanHistory.count())
            .from(userLoanHistory)
            .where(
                userLoanHistory.status.eq(status)
            )
            .fetchOne() ?: 0L
    }
}