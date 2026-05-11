package me.nikola.stackoverflowusers.data.repository

import kotlinx.coroutines.CancellationException
import me.nikola.stackoverflowusers.data.local.LocalDataSource
import me.nikola.stackoverflowusers.data.mapper.toStackOverflowUsers
import me.nikola.stackoverflowusers.data.remote.StackOverflowApi
import me.nikola.stackoverflowusers.domain.model.StackOverflowUser
import me.nikola.stackoverflowusers.domain.repository.UsersRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val api: StackOverflowApi,
    private val localDataSource: LocalDataSource,
) : UsersRepository {

    override suspend fun getTopUsers(): Result<List<StackOverflowUser>> =
        try {
            val followedUserIds = localDataSource.getFollowedUserIds()
            val users = api.getTopUsers(pageSize = TOP_USERS_LIMIT)
                .items
                .toStackOverflowUsers()
                .map { user ->
                    user.copy(isFollowed = user.id in followedUserIds)
                }

            Result.success(users)
        } catch (error: CancellationException) {
            throw error
        } catch (error: Throwable) {
            Result.failure(error)
        }

    override suspend fun followUser(userId: Long) {
        localDataSource.followUser(userId)
    }

    override suspend fun unfollowUser(userId: Long) {
        localDataSource.unfollowUser(userId)
    }

    private companion object {
        const val TOP_USERS_LIMIT = 20
    }
}
