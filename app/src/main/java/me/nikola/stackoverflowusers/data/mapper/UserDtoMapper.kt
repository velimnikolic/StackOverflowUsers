package me.nikola.stackoverflowusers.data.mapper

import me.nikola.stackoverflowusers.data.remote.dto.UserDto
import me.nikola.stackoverflowusers.domain.model.StackOverflowUser

fun UserDto.toStackOverflowUser(): StackOverflowUser =
    StackOverflowUser(
        id = userId,
        name = displayName,
        profileImageUrl = profileImage.orEmpty(),
        reputation = reputation,
        isFollowed = false,
    )

fun List<UserDto>.toStackOverflowUsers(): List<StackOverflowUser> =
    map(UserDto::toStackOverflowUser)
