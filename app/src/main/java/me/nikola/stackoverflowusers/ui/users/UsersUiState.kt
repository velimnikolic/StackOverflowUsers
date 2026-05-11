package me.nikola.stackoverflowusers.ui.users

import me.nikola.stackoverflowusers.domain.model.StackOverflowUser

data class UsersUiState(
    val isLoading: Boolean = false,
    val users: List<StackOverflowUser> = emptyList(),
    val errorMessage: String? = null,
)
