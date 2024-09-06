package com.devpaik.library.controller.book

import com.devpaik.library.dto.book.request.BookLoanRequest
import com.devpaik.library.dto.book.request.BookRequest
import com.devpaik.library.dto.book.request.BookReturnRequest
import com.devpaik.library.dto.book.response.BookStatResponse
import com.devpaik.library.service.book.BookService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/book")
class BookController(
    private val bookService: BookService
) {

    @PostMapping
    fun saveBook(@RequestBody request: BookRequest) {

        bookService.saveBook(request)
    }

    @PostMapping("/loan")
    fun loanBook(@RequestBody request: BookLoanRequest) {
        bookService.loanBook(request)
    }

    @PutMapping("/return")
    fun returnBook(@RequestBody request: BookReturnRequest) {
        bookService.returnBook(request)
    }

    @GetMapping("/loan")
    fun countLoanBook() : Long {
        return bookService.countLoanBook();
    }

    @GetMapping("/stat")
    fun getBookStatistics() : List<BookStatResponse> {
        return bookService.getBookStatistics();
    }
}
