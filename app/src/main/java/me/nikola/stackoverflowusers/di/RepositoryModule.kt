package me.nikola.stackoverflowusers.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.nikola.stackoverflowusers.data.repository.StackOverflowUsersRepository
import me.nikola.stackoverflowusers.domain.repository.UsersRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUsersRepository(
        repository: StackOverflowUsersRepository,
    ): UsersRepository
}
