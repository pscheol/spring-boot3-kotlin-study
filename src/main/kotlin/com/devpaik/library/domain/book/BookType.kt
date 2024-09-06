package com.devpaik.library.domain.book

enum class BookType {
    SCIENCE,
    SOCIETY,
    ECONOMY,
    COMPUTER,
    LANGUAGE;

    fun isTypeEquals(type: BookType): Boolean {
        return this == type
    }
}