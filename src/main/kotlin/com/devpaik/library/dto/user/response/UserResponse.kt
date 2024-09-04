package com.devpaik.library.dto.user.response

import com.devpaik.library.domain.user.User

data class UserResponse(
    val id: Long,
    val name: String,
    val age: Int?
) {

    //정적 팩토리 방식
    companion object {
        fun of (user: User): UserResponse {
            return UserResponse(
                id = user.id!!,
                name = user.name,
                age = user.age
            )
        }
    }
//    constructor(user: User) : this(
//        id = user.id!!,
//        name = user.name,
//        age = user.age
//    )
}
