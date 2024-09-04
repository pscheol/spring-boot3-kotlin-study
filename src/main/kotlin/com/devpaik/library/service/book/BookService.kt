package com.devpaik.library.service.book

import com.devpaik.library.domain.book.Book
import com.devpaik.library.domain.book.BookRepository
import com.devpaik.library.domain.user.UserRepository
import com.devpaik.library.domain.user.loanhistory.UserLoanHistoryRepository
import com.devpaik.library.domain.user.loanhistory.UserLoanStatus
import com.devpaik.library.dto.book.request.BookLoanRequest
import com.devpaik.library.dto.book.request.BookRequest
import com.devpaik.library.dto.book.request.BookReturnRequest
import com.devpaik.library.utils.fail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService (
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

    @Transactional
    fun saveBook(request: BookRequest) {
        val newBook = Book(request.name, request.type);
        bookRepository.save(newBook);
    }

    @Transactional
    fun loanBook(request: BookLoanRequest) {
        val book = bookRepository.findByName(request.bookName) ?: fail()
        if (userLoanHistoryRepository.findByBookNameAndStatus(request.bookName, UserLoanStatus.LOANED) != null) {
            throw IllegalArgumentException("진작 대출되어 있는 책입니다");
        }
        val user = userRepository.findByName(request.userName) ?: fail()
        user.loanBook(book)
    }

    @Transactional
    fun returnBook(request: BookReturnRequest) {
        val user = userRepository.findByName(request.userName) ?: fail()
        user.returnBook(request.bookName)
    }
}