package me.nikola.stackoverflowusers.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.nikola.stackoverflowusers.data.local.LocalDataSource
import me.nikola.stackoverflowusers.data.local.SharedPreferencesLocalDataSource
import me.nikola.stackoverflowusers.data.repository.UsersRepositoryImpl
import me.nikola.stackoverflowusers.domain.repository.UsersRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUsersRepository(
        repository: UsersRepositoryImpl,
    ): UsersRepository

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(
        dataSource: SharedPreferencesLocalDataSource,
    ): LocalDataSource
}
