package me.nikola.stackoverflowusers.ui.users

import org.junit.Assert.assertEquals
import org.junit.Test

class UsersViewModelTest {

    @Test
    fun `initial ui state is not loading`() {
        val viewModel = UsersViewModel()

        assertEquals(
            UsersUiState(isLoading = false),
            viewModel.uiState.value,
        )
    }
}
