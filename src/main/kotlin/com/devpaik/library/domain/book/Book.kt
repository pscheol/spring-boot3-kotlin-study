package com.devpaik.library.domain.book

import jakarta.persistence.*


@Entity
@Table(name = "tb_book")
class Book(
    @Column
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column
    val type: BookType,
) {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    val id: Long? = null

    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다.")
        }
    }

    companion object {
        fun fixture(
            name: String = "책 이름",
            type: BookType = BookType.COMPUTER,
        ) : Book {

        return Book(
            name = name,
            type = type)
        }
    }

}