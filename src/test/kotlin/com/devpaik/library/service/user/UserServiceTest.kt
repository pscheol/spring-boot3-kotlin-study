package com.devpaik.library.service.user;

import com.devpaik.library.domain.book.BookRepository
import com.devpaik.library.domain.user.User
import com.devpaik.library.domain.user.UserRepository
import com.devpaik.library.domain.user.loanhistory.UserLoanHistory
import com.devpaik.library.domain.user.loanhistory.UserLoanHistoryRepository
import com.devpaik.library.domain.user.loanhistory.UserLoanStatus
import com.devpaik.library.dto.user.request.UserCreateRequest
import com.devpaik.library.dto.user.request.UserUpdateRequest
import com.devpaik.library.service.book.BookService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {
    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var bookService: BookService

    @AfterEach
    fun clean() {
        println("Clean start")
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

    @DisplayName("사용자 대출 내역 조회(대출 기록이 없는 유저도 응답에 포함)")
    @Test
    fun userLoanHistoriesTest1() {
        //given
        userRepository.save(User("A", null))
        //when
        val result = userService.getUserLoanHistories();

        //then
        assertThat(result).hasSize(1);
        assertThat(result[0].name).isEqualTo("A")
        assertThat(result[0].books.isEmpty())
    }

    @DisplayName("사용자 대출 내역 조회(대출 기록이 있는 데이터 응답)")
    @Test
    fun userLoanHistoriesTest2() {
        //given
        val saveUser = userRepository.save(User("A", null))

        userLoanHistoryRepository.saveAll(listOf(
            UserLoanHistory.fixture(saveUser, "Book1", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(saveUser, "Book2", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(saveUser, "Book3", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(saveUser, "Book4", UserLoanStatus.RETURNED)
        ))
        //when
        val result = userService.getUserLoanHistories();

        //then
        assertThat(result).hasSize(1);
        assertThat(result[0].name).isEqualTo("A")
        assertThat(result[0].books).hasSize(4)
        assertThat(result[0].books).extracting("name").containsExactlyInAnyOrder("Book1", "Book2", "Book3", "Book4")
        assertThat(result[0].books).extracting("isReturn").containsExactlyInAnyOrder(false, false, false, true)
    }

    @DisplayName("사용자 대출 내역 조회(2명 이상)")
    @Test
    fun userLoanHistoriesTest3() {
        //given
        val saveUsers = userRepository.saveAll(listOf(
            User("A", null),
            User("B", null))
        )

        userLoanHistoryRepository.saveAll(listOf(
            UserLoanHistory.fixture(saveUsers[0], "Book1", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(saveUsers[0], "Book2", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(saveUsers[0], "Book3", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(saveUsers[0], "Book4", UserLoanStatus.RETURNED),
            UserLoanHistory.fixture(saveUsers[1], "Book5", UserLoanStatus.LOANED),
        ))
        //when
        val result = userService.getUserLoanHistories();

        //then
        assertThat(result).hasSize(2);
        assertThat(result[0].name).isEqualTo("A")
        assertThat(result[0].books).hasSize(4)
        assertThat(result[0].books).extracting("name").containsExactlyInAnyOrder("Book1", "Book2", "Book3", "Book4")
        assertThat(result[0].books).extracting("isReturn").containsExactlyInAnyOrder(false, false, false, true)


        assertThat(result[1].name).isEqualTo("B")
        assertThat(result[1].books).hasSize(1)
        assertThat(result[1].books).extracting("name").containsExactlyInAnyOrder("Book5")
        assertThat(result[1].books).extracting("isReturn").containsExactlyInAnyOrder(false)
    }
}