package com.devpaik.library.domain.book

import com.devpaik.library.domain.book.QBook.book
import com.devpaik.library.dto.book.response.BookStatResponse
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class BookQueryDSLRepository(
    private val queryFactory: JPAQueryFactory,
) {

    fun getStatList(): List<BookStatResponse> {
        return queryFactory.select(Projections.constructor(
                BookStatResponse::class.java,
                book.type,
                book.count()
                )
            )
            .from(book)
            .groupBy(book.type)
            .fetch()
    }
}