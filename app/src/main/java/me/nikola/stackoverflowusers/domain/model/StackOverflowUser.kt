package me.nikola.stackoverflowusers.domain.model

data class StackOverflowUser(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
    val reputation: Int,
    val isFollowed: Boolean,
)
