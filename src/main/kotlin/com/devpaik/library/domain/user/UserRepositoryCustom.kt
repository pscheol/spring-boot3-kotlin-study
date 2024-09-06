package com.devpaik.library.domain.user

import com.devpaik.library.domain.user.loanhistory.UserLoanHistory
import com.devpaik.library.domain.user.loanhistory.UserLoanStatus

interface UserRepositoryCustom {

    fun findAllByWithHistories(): List<User>
}