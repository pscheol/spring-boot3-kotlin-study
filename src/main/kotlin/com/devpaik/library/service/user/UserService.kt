package com.devpaik.library.service.user

import com.devpaik.library.domain.user.User
import com.devpaik.library.domain.user.UserRepository
import com.devpaik.library.dto.user.request.UserCreateRequest
import com.devpaik.library.dto.user.request.UserUpdateRequest
import com.devpaik.library.dto.user.response.UserLoanHistoryResponse
import com.devpaik.library.dto.user.response.UserResponse
import com.devpaik.library.utils.fail
import com.devpaik.library.utils.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService (
    private val userRepository: UserRepository,
) {

    @Transactional
    fun saveUser(request: UserCreateRequest) {
        val newUser = User(request.name, request.age)
        userRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    fun getUsers(): List<UserResponse> {
        return userRepository.findAll().map(UserResponse::of)
    }

    @Transactional
    fun updateUserName(request : UserUpdateRequest) {
        val user = userRepository.findByIdOrThrow(request.id)
        user.updateName(request.name)

    }

    @Transactional
    fun deleteUser(name: String) {
        val user = userRepository.findByName(name) ?: fail()
        userRepository.delete(user)
    }

    @Transactional(readOnly = true)
    fun getUserLoanHistories(): List<UserLoanHistoryResponse> {
        return userRepository.findAllByWithHistories().map(UserLoanHistoryResponse::of)
    }

}