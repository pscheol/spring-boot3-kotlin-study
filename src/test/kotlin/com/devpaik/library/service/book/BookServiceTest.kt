package com.devpaik.library.service.book;

import com.devpaik.library.domain.book.Book
import com.devpaik.library.domain.book.BookRepository
import com.devpaik.library.domain.book.BookType
import com.devpaik.library.domain.user.User
import com.devpaik.library.domain.user.UserRepository
import com.devpaik.library.domain.user.loanhistory.UserLoanHistory
import com.devpaik.library.domain.user.loanhistory.UserLoanHistoryRepository
import com.devpaik.library.domain.user.loanhistory.UserLoanStatus
import com.devpaik.library.dto.book.request.BookLoanRequest
import com.devpaik.library.dto.book.request.BookRequest
import com.devpaik.library.dto.book.request.BookReturnRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository

) {

    @AfterEach
    fun tearDown() {
        bookRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("책 등록한다.")
    @Test
    fun saveBook() {
        //given
        val request = BookRequest("Test Book", BookType.COMPUTER)
        //when
        bookService.saveBook(request)

        //then
        val books = bookRepository.findAll();

        assertThat(books).hasSize(1);
        assertThat(books[0].name).isEqualTo("Test Book");
        assertThat(books[0].type).isEqualTo(BookType.COMPUTER);

    }

    @DisplayName("책 대출이 정상 작동한다.")
    @Test
    fun loanBook() {
        //given
        bookRepository.save(Book.fixture("이상한 나라의 엘리스"))
        val user = userRepository.save(User("honggil", null))
        val request = BookLoanRequest("honggil", "이상한 나라의 엘리스")

        //when
        bookService.loanBook(request)

        //then
        val books = bookRepository.findAll()

        //then
        val results = userLoanHistoryRepository.findAll();
        assertThat(results).hasSize(1);
        assertThat(results[0].bookName).isEqualTo("이상한 나라의 엘리스")
        assertThat(results[0].user.id).isEqualTo(user.id)
        assertThat(results[0].status).isEqualTo(UserLoanStatus.LOANED)
    }

    @DisplayName("책이 진작 대출되어 있다면, 신규대출이 실패한다.")
    @Test
    fun loanBookFailTest() {
        //given
        bookRepository.save(Book.fixture("이상한 나라의 엘리스"));
        val user = userRepository.save(User("honggil", null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(user, "이상한 나라의 엘리스"))
        val request = BookLoanRequest("honggil", "이상한 나라의 엘리스")
        //when & then
        val message = assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.message

        assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")

    }
    @DisplayName("책 반납이 정상적으로 된다.")
    @Test
    fun returnBook() {
        //given
        bookRepository.save(Book.fixture("이상한 나라의 엘리스"))
        val user = userRepository.save(User("honggil", null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(user, "이상한 나라의 엘리스"))

        val request = BookReturnRequest("honggil", "이상한 나라의 엘리스")
        //when
        bookService.returnBook(request)

        //then
        val results = userLoanHistoryRepository.findAll();
        assertThat(results).hasSize(1);
        assertThat(results[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }
}