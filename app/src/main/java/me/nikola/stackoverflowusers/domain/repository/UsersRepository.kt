package me.nikola.stackoverflowusers.domain.repository

import me.nikola.stackoverflowusers.domain.model.StackOverflowUser

interface UsersRepository {
    suspend fun getTopUsers(): Result<List<StackOverflowUser>>
    suspend fun followUser(userId: Long)
    suspend fun unfollowUser(userId: Long)
}
