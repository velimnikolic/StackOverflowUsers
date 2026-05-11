package me.nikola.stackoverflowusers.data.mapper

import me.nikola.stackoverflowusers.data.remote.dto.UserDto
import me.nikola.stackoverflowusers.domain.model.StackOverflowUser
import org.junit.Assert.assertEquals
import org.junit.Test

class UserDtoMapperTest {

    @Test
    fun `maps user dto to stack overflow user`() {
        val dto = UserDto(
            userId = 123L,
            displayName = "Nikola",
            profileImage = "https://example.com/profile.png",
            reputation = 42,
        )

        val user = dto.toStackOverflowUser()

        assertEquals(
            StackOverflowUser(
                id = 123L,
                name = "Nikola",
                profileImageUrl = "https://example.com/profile.png",
                reputation = 42,
                isFollowed = false,
            ),
            user,
        )
    }

    @Test
    fun `maps null profile image to empty profile image url`() {
        val dto = UserDto(
            userId = 123L,
            displayName = "Nikola",
            profileImage = null,
            reputation = 42,
        )

        val user = dto.toStackOverflowUser()

        assertEquals("", user.profileImageUrl)
    }

    @Test
    fun `maps user dto list to stack overflow user list`() {
        val dtos = listOf(
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
        )

        val users = dtos.toStackOverflowUsers()

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
                    isFollowed = false,
                ),
            ),
            users,
        )
    }
}
