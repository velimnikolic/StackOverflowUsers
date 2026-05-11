package me.nikola.stackoverflowusers.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.nikola.stackoverflowusers.domain.repository.UsersRepository
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UsersRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersUiState(isLoading = true))
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    init {
        loadUsers()
    }

    fun retry() {
        loadUsers()
    }

    fun followUser(userId: Long) {
        viewModelScope.launch {
            repository.followUser(userId)
            updateFollowedState(userId = userId, isFollowed = true)
        }
    }

    fun unfollowUser(userId: Long) {
        viewModelScope.launch {
            repository.unfollowUser(userId)
            updateFollowedState(userId = userId, isFollowed = false)
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            repository.getTopUsers()
                .onSuccess { users ->
                    _uiState.value = UsersUiState(users = users)
                }
                .onFailure { error ->
                    if (error is CancellationException) throw error
                    _uiState.value = UsersUiState(
                        errorMessage = "Unable to load StackOverflow users. Check your connection and try again.",
                    )
                }
        }
    }

    private fun updateFollowedState(userId: Long, isFollowed: Boolean) {
        _uiState.update { state ->
            state.copy(
                users = state.users.map { user ->
                    if (user.id == userId) user.copy(isFollowed = isFollowed) else user
                },
            )
        }
    }
}
