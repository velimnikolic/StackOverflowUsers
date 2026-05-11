package me.nikola.stackoverflowusers.data.remote.dto

import com.squareup.moshi.Json

data class UserDto(
    @param:Json(name = "user_id")
    val userId: Long,
    @param:Json(name = "display_name")
    val displayName: String,
    @param:Json(name = "profile_image")
    val profileImage: String?,
    val reputation: Int,
)
