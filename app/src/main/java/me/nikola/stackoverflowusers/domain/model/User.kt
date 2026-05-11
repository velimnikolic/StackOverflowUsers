package me.nikola.stackoverflowusers.domain.model

data class User(
    val id: Long,
    val displayName: String,
    val avatarUrl: String?,
)
