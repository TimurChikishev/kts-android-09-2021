package com.swallow.cracker.di

import android.content.Context
import com.swallow.cracker.data.database.RedditDatabase
import com.swallow.cracker.data.network.AuthInterceptor
import com.swallow.cracker.data.repository.AuthRepositoryImpl
import com.swallow.cracker.data.repository.OnBoardingRepositoryImpl
import com.swallow.cracker.data.repository.RedditRepositoryImpl
import com.swallow.cracker.data.repository.UserPreferencesRepositoryImpl
import com.swallow.cracker.domain.repository.AuthRepository
import com.swallow.cracker.domain.repository.OnBoardingRepository
import com.swallow.cracker.domain.repository.RedditRepository
import com.swallow.cracker.domain.repository.UserPreferencesRepository
import com.swallow.cracker.domain.usecase.AuthUseCase
import com.swallow.cracker.domain.usecase.GetPostsUseCase
import com.swallow.cracker.domain.usecase.OnBoardingUseCase
import com.swallow.cracker.domain.usecase.UserPreferencesUseCase
import com.swallow.cracker.ui.viewmodels.*
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {

    viewModel { RedditListViewModel(get()) }

    viewModel { PostDetailViewModel(get()) }

    viewModel { ProfileViewModel(get(), get(), get()) }

    viewModel { OnBoardingViewModel(get(), get()) }

    viewModel { AuthViewModel(androidApplication(), get(), get(), get()) }

    viewModel { SearchViewModel(get()) }

    viewModel { SubredditViewModel(get(), get()) }

    single { providesGetPostsUseCase(get()) }

    single { providesRedditRepository(get()) }

    single { providesRedditDatabase(androidContext()) }

    single { providesUserPreferencesUseCase(get()) }

    single { providesUserPreferencesRepository(androidContext()) }

    single { providesOnBoardingUseCase(get()) }

    single { providesOnBoardingRepository() }

    single { providesAuthInterceptor(get()) }

    single { providesAuthUseCase(get()) }

    single { providesAuthRepository() }
}


fun providesRedditDatabase(context: Context): RedditDatabase {
    return RedditDatabase.create(context)
}

fun providesRedditRepository(redditDatabase: RedditDatabase): RedditRepository {
    return RedditRepositoryImpl(redditDatabase)
}

fun providesGetPostsUseCase(redditRepository: RedditRepository): GetPostsUseCase {
    return GetPostsUseCase(redditRepository)
}

fun providesUserPreferencesRepository(context: Context): UserPreferencesRepository {
    return UserPreferencesRepositoryImpl(context)
}

fun providesOnBoardingUseCase(repository: OnBoardingRepository): OnBoardingUseCase {
    return OnBoardingUseCase(repository)
}

fun providesOnBoardingRepository(): OnBoardingRepository {
    return OnBoardingRepositoryImpl()
}

fun providesAuthInterceptor(userPreferencesUseCase: UserPreferencesUseCase): AuthInterceptor {
    return AuthInterceptor(userPreferencesUseCase)
}

fun providesAuthUseCase(authRepository: AuthRepository): AuthUseCase {
    return AuthUseCase(authRepository)
}

fun providesAuthRepository(): AuthRepository {
    return AuthRepositoryImpl()
}