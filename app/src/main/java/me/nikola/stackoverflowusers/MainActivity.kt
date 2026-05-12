package me.nikola.stackoverflowusers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import me.nikola.stackoverflowusers.ui.theme.StackOverflowUsersTheme
import me.nikola.stackoverflowusers.ui.users.UsersRoute
import me.nikola.stackoverflowusers.ui.users.UsersViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StackOverflowUsersTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                UsersRoute(
                    uiState = uiState,
                    onFollowClick = viewModel::followUser,
                    onUnfollowClick = viewModel::unfollowUser,
                    onRetryClick = viewModel::retry,
                )
            }
        }
    }
}
