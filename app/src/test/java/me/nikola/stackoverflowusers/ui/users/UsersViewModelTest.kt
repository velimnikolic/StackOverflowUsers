package me.nikola.stackoverflowusers.ui.users

import java.io.IOException
import kotlinx.coroutines.test.runTest
import me.nikola.stackoverflowusers.domain.model.StackOverflowUser
import me.nikola.stackoverflowusers.domain.repository.UsersRepository
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class UsersViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `init loads users from repository`() = runTest {
        val users = listOf(user(id = 1L), user(id = 2L, isFollowed = true))
        val viewModel = UsersViewModel(
            repository = FakeUsersRepository(getTopUsersResult = Result.success(users)),
        )

        assertEquals(
            UsersUiState(users = users),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `init shows error empty state when repository fails`() = runTest {
        val viewModel = UsersViewModel(
            repository = FakeUsersRepository(
                getTopUsersResult = Result.failure(IOException("Offline")),
            ),
        )

        assertEquals(true, viewModel.uiState.value.users.isEmpty())
        assertEquals(false, viewModel.uiState.value.isLoading)
        assertEquals(
            "Unable to load StackOverflow users. Check your connection and try again.",
            viewModel.uiState.value.errorMessage,
        )
    }

    @Test
    fun `followUser persists and updates user state`() = runTest {
        val repository = FakeUsersRepository(
            getTopUsersResult = Result.success(listOf(user(id = 1L))),
        )
        val viewModel = UsersViewModel(repository = repository)

        viewModel.followUser(1L)

        assertEquals(listOf(1L), repository.followedUserIds)
        assertEquals(true, viewModel.uiState.value.users.first().isFollowed)
    }

    @Test
    fun `unfollowUser persists and updates user state`() = runTest {
        val repository = FakeUsersRepository(
            getTopUsersResult = Result.success(listOf(user(id = 1L, isFollowed = true))),
        )
        val viewModel = UsersViewModel(repository = repository)

        viewModel.unfollowUser(1L)

        assertEquals(listOf(1L), repository.unfollowedUserIds)
        assertEquals(false, viewModel.uiState.value.users.first().isFollowed)
    }

    private fun user(
        id: Long,
        isFollowed: Boolean = false,
    ): StackOverflowUser =
        StackOverflowUser(
            id = id,
            name = "User $id",
            profileImageUrl = "https://example.com/$id.png",
            reputation = id.toInt() * 100,
            isFollowed = isFollowed,
        )

    private class FakeUsersRepository(
        private val getTopUsersResult: Result<List<StackOverflowUser>>,
    ) : UsersRepository {

        val followedUserIds = mutableListOf<Long>()
        val unfollowedUserIds = mutableListOf<Long>()

        override suspend fun getTopUsers(): Result<List<StackOverflowUser>> = getTopUsersResult

        override suspend fun followUser(userId: Long) {
            followedUserIds += userId
        }

        override suspend fun unfollowUser(userId: Long) {
            unfollowedUserIds += userId
        }
    }
}
