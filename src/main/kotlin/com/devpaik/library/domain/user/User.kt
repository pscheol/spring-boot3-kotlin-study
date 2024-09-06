package com.devpaik.library.domain.user

import com.devpaik.library.domain.book.Book
import com.devpaik.library.domain.user.loanhistory.UserLoanHistory
import jakarta.persistence.*

@Entity
@Table(name = "tb_user")
class User(
    name: String,
    age: Int?,
) {
    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다.");
        }
    }

    @Column
    var name: String = name
        private set

    @Column
    var age: Int? = age
        private set

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val userLoanHistories: MutableList<UserLoanHistory>  = mutableListOf()


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    val id: Long? = null

    fun updateName(name: String) {
        this.name = name;
    }

    fun loanBook(book : Book) {
        this.userLoanHistories.add(UserLoanHistory.fixture(this, book.name));
    }

    fun returnBook(bookName: String) {
        this.userLoanHistories.first{ history -> history.bookName == bookName}
            .doReturn()

    }
}