package me.nikola.stackoverflowusers.data.remote

import me.nikola.stackoverflowusers.data.remote.dto.UsersResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface StackOverflowApi {

    @GET("users")
    suspend fun getTopUsers(
        @Query("page") page: Int = 1,
        @Query("pagesize") pageSize: Int = 20,
        @Query("order") order: String = "desc",
        @Query("sort") sort: String = "reputation",
        @Query("site") site: String = "stackoverflow",
    ): UsersResponseDto
}
