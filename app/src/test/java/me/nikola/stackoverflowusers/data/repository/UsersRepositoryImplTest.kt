package me.nikola.stackoverflowusers.data.repository

import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import me.nikola.stackoverflowusers.data.local.LocalDataSource
import me.nikola.stackoverflowusers.data.remote.StackOverflowApi
import me.nikola.stackoverflowusers.data.remote.dto.UserDto
import me.nikola.stackoverflowusers.data.remote.dto.UsersResponseDto
import me.nikola.stackoverflowusers.domain.model.StackOverflowUser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class UsersRepositoryImplTest {

    @Test
    fun `getTopUsers fetches top 20 users and maps dtos to domain models`() = runTest {
        val api = FakeStackOverflowApi(
            response = UsersResponseDto(
                items = listOf(
                    UserDto(
                        userId = 1L,
                        displayName = "Ada",
                        profileImage = "https://example.com/ada.png",
                        reputation = 100,
                    ),
                    UserDto(
                        userId = 2L,
                        displayName = "Grace",
                        profileImage = null,
                        reputation = 200,
                    ),
                ),
            ),
        )
        val repository = UsersRepositoryImpl(
            api = api,
            localDataSource = FakeLocalDataSource(followedUserIds = setOf(2L)),
        )

        val result = repository.getTopUsers()

        assertTrue(result.isSuccess)
        assertEquals(
            listOf(
                StackOverflowUser(
                    id = 1L,
                    name = "Ada",
                    profileImageUrl = "https://example.com/ada.png",
                    reputation = 100,
                    isFollowed = false,
                ),
                StackOverflowUser(
                    id = 2L,
                    name = "Grace",
                    profileImageUrl = "",
                    reputation = 200,
                    isFollowed = true,
                ),
            ),
            result.getOrThrow(),
        )
        assertEquals(1, api.lastPage)
        assertEquals(20, api.lastPageSize)
        assertEquals("desc", api.lastOrder)
        assertEquals("reputation", api.lastSort)
        assertEquals("stackoverflow", api.lastSite)
    }

    @Test
    fun `getTopUsers returns failure when api throws`() = runTest {
        val expectedError = IOException("Network unavailable")
        val repository = UsersRepositoryImpl(
            api = FakeStackOverflowApi(error = expectedError),
            localDataSource = FakeLocalDataSource(),
        )

        val result = repository.getTopUsers()

        assertTrue(result.isFailure)
        assertSame(expectedError, result.exceptionOrNull())
    }

    @Test(expected = CancellationException::class)
    fun `getTopUsers preserves coroutine cancellation`() = runTest {
        val repository = UsersRepositoryImpl(
            api = FakeStackOverflowApi(error = CancellationException("Cancelled")),
            localDataSource = FakeLocalDataSource(),
        )

        repository.getTopUsers()
    }

    @Test
    fun `followUser persists followed user id locally`() = runTest {
        val localDataSource = FakeLocalDataSource()
        val repository = UsersRepositoryImpl(
            api = FakeStackOverflowApi(),
            localDataSource = localDataSource,
        )

        repository.followUser(42L)

        assertEquals(setOf(42L), localDataSource.getFollowedUserIds())
    }

    @Test
    fun `unfollowUser removes followed user id locally`() = runTest {
        val localDataSource = FakeLocalDataSource(followedUserIds = setOf(42L))
        val repository = UsersRepositoryImpl(
            api = FakeStackOverflowApi(),
            localDataSource = localDataSource,
        )

        repository.unfollowUser(42L)

        assertEquals(emptySet<Long>(), localDataSource.getFollowedUserIds())
    }

    private class FakeStackOverflowApi(
        private val response: UsersResponseDto = UsersResponseDto(items = emptyList()),
        private val error: Throwable? = null,
    ) : StackOverflowApi {

        var lastPage: Int? = null
            private set
        var lastPageSize: Int? = null
            private set
        var lastOrder: String? = null
            private set
        var lastSort: String? = null
            private set
        var lastSite: String? = null
            private set

        override suspend fun getTopUsers(
            page: Int,
            pageSize: Int,
            order: String,
            sort: String,
            site: String,
        ): UsersResponseDto {
            lastPage = page
            lastPageSize = pageSize
            lastOrder = order
            lastSort = sort
            lastSite = site

            error?.let { throw it }

            return response
        }
    }

    private class FakeLocalDataSource(
        followedUserIds: Set<Long> = emptySet(),
    ) : LocalDataSource {

        private val followedUserIds = followedUserIds.toMutableSet()

        override fun getFollowedUserIds(): Set<Long> = followedUserIds.toSet()

        override fun followUser(userId: Long) {
            followedUserIds += userId
        }

        override fun unfollowUser(userId: Long) {
            followedUserIds -= userId
        }
    }
}
