package com.devpaik.library.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Long> {
    fun findByName(name: String): User?


    @Query("""
        select distinct u
        from User u left join fetch u.userLoanHistories 
    """)
    fun findAllByWithHistories(): List<User>
}