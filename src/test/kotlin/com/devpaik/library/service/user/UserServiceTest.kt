package com.devpaik.library.service.user;

import com.devpaik.library.domain.user.User
import com.devpaik.library.domain.user.UserRepository
import com.devpaik.library.dto.user.request.UserCreateRequest
import com.devpaik.library.dto.user.request.UserUpdateRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService
) {

    @AfterEach
    fun clean() {
        userRepository.deleteAll();
    }

    @DisplayName("사용자 저장할 수 있다.")
    @Test
    fun saveUser() {
        //given
        val request = UserCreateRequest("홍길동", null);

        //when
        userService.saveUser(request);

        //then
        val result = userRepository.findAll();
        assert(result.size == 1)
        assertThat(result).hasSize(1);
        assertThat(result[0].name).isEqualTo("홍길동");
        assertThat(result[0].age).isNull();

    }

    @DisplayName("사용자 조회 테스트")
    @Test
    fun getUsers() {
        //given
        userRepository.saveAll(listOf(
            User("A", 20),
            User("B", null)
        ));

        //when
        val result = userService.getUsers();

        //then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("name").containsExactlyInAnyOrder("A", "B");
        assertThat(result).extracting("age").containsExactlyInAnyOrder(20, null)
    }
    @DisplayName("사용자를 삭제한다.")
    @Test
    fun updateUserName() {
        //given
        val user = userRepository.save(User("A", null));
        val request = UserUpdateRequest(user.id!!, "B");
        //when
        userService.updateUserName(request);

        //then
        val updatedUser = userRepository.findById(user.id!!).get();
        assertThat(updatedUser.name).isEqualTo("B");
    }

    @DisplayName("사용자를 삭제한다.")
    @Test
    fun deleteUser() {
        //given
        val user = userRepository.save(User("A", null));
        //when
        userService.deleteUser(user.name);

        //then
        assertThat(userRepository.findById(user.id!!)).isEmpty();
    }
}