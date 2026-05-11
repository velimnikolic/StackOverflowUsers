package me.nikola.stackoverflowusers.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface LocalDataSource {
    fun getFollowedUserIds(): Set<Long>
    fun followUser(userId: Long)
    fun unfollowUser(userId: Long)
}

@Singleton
class SharedPreferencesLocalDataSource @Inject constructor(
    @ApplicationContext context: Context,
) : LocalDataSource {

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun getFollowedUserIds(): Set<Long> =
        preferences.getStringSet(FOLLOWED_USER_IDS_KEY, emptySet())
            .orEmpty()
            .mapNotNull(String::toLongOrNull)
            .toSet()

    override fun followUser(userId: Long) {
        updateFollowedUserIds { currentIds -> currentIds + userId }
    }

    override fun unfollowUser(userId: Long) {
        updateFollowedUserIds { currentIds -> currentIds - userId }
    }

    private fun updateFollowedUserIds(transform: (Set<Long>) -> Set<Long>) {
        val updatedIds = transform(getFollowedUserIds()).map(Long::toString).toSet()
        preferences.edit()
            .putStringSet(FOLLOWED_USER_IDS_KEY, updatedIds)
            .apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "stack_overflow_users_preferences"
        const val FOLLOWED_USER_IDS_KEY = "followed_user_ids"
    }
}
