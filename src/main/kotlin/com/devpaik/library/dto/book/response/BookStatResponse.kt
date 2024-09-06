package com.devpaik.library.dto.book.response

import com.devpaik.library.domain.book.BookType

data class BookStatResponse(
    val type: BookType,
    val count: Long
) {
}
