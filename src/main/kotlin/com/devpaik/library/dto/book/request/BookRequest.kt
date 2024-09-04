package com.devpaik.library.dto.book.request

import com.devpaik.library.domain.book.BookType

data class BookRequest(
    val name: String,
    val type: BookType,
)
