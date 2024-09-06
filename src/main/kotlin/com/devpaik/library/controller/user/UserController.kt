package com.devpaik.library.controller.user

import com.devpaik.library.dto.user.request.UserCreateRequest
import com.devpaik.library.dto.user.request.UserUpdateRequest
import com.devpaik.library.dto.user.response.UserLoanHistoryResponse
import com.devpaik.library.dto.user.response.UserResponse
import com.devpaik.library.service.user.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun saveUser(@RequestBody request: UserCreateRequest) {
        userService.saveUser(request)
    }


    @get:GetMapping
    val users: List<UserResponse>
        get() = userService.getUsers()

    @PutMapping
    fun updateUserName(@RequestBody request: UserUpdateRequest) {
        userService.updateUserName(request)
    }

    @DeleteMapping
    fun deleteUser(@RequestParam name: String) {
        userService.deleteUser(name)
    }

    @GetMapping("/loan")
    fun getUserLoanHistories(): List<UserLoanHistoryResponse> {
        return userService.getUserLoanHistories()
    }
}
