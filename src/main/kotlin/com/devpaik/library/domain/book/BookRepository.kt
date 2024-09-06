package com.devpaik.library.domain.book

import com.devpaik.library.dto.book.response.BookStatResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BookRepository : JpaRepository<Book, Long> {
    fun findByName(name: String): Book?


    @Query("""
        select new com.devpaik.library.dto.book.response.BookStatResponse(b.type, count(b.id)) 
          from Book b 
      group by b.type
    """)
    fun getStatList() : List<BookStatResponse>
}