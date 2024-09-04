package com.devpaik.library.controller.user

import com.devpaik.library.dto.user.request.UserCreateRequest
import com.devpaik.library.dto.user.request.UserUpdateRequest
import com.devpaik.library.dto.user.response.UserResponse
import com.devpaik.library.service.user.UserService
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("/user")
    fun saveUser(@RequestBody request: UserCreateRequest) {
        userService.saveUser(request)
    }


    @get:GetMapping("/user")
    val users: List<UserResponse>
        get() = userService.getUsers()

    @PutMapping("/user")
    fun updateUserName(@RequestBody request: UserUpdateRequest) {
        userService.updateUserName(request)
    }

    @DeleteMapping("/user")
    fun deleteUser(@RequestParam name: String) {
        userService.deleteUser(name)
    }
}
