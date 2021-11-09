package com.swallow.cracker.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.swallow.cracker.data.config.DataStoreConfig
import com.swallow.cracker.data.database.RedditDatabase
import com.swallow.cracker.data.network.AuthInterceptor
import com.swallow.cracker.data.repository.*
import com.swallow.cracker.domain.repository.AuthRepository
import com.swallow.cracker.domain.repository.OnBoardingRepository
import com.swallow.cracker.domain.repository.RedditRepository
import com.swallow.cracker.domain.repository.UserPreferencesRepository
import com.swallow.cracker.domain.usecase.AuthUseCase
import com.swallow.cracker.domain.usecase.GetPostsUseCase
import com.swallow.cracker.domain.usecase.OnBoardingUseCase
import com.swallow.cracker.domain.usecase.UserPreferencesUseCase
import com.swallow.cracker.ui.viewmodels.*
import net.openid.appauth.AuthorizationService
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {

    viewModel { RedditListViewModel(get(), get()) }

    viewModel { PostDetailViewModel(get()) }

    viewModel { ProfileViewModel(get(), get(), get()) }

    viewModel { OnBoardingViewModel(get(), get()) }

    viewModel { AuthViewModel(get(), get(), get(), get()) }

    viewModel { SearchViewModel(get()) }

    viewModel { SubscriptionsViewModel(get()) }

    viewModel { SubredditViewModel(get(), get()) }

    single { GetPostsUseCase(get()) }

    single<RedditRepository> { RedditRepositoryImpl(get(), get()) }

    single { RedditDatabase.create(androidContext()) }

    single { UserPreferencesUseCase(get()) }

    single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(get()) }

    single { OnBoardingUseCase(get()) }

    single<OnBoardingRepository> { OnBoardingRepositoryImpl() }

    single { AuthInterceptor(get()) }

    single { AuthUseCase(get()) }

    single<AuthRepository> { AuthRepositoryImpl() }

    single { AuthorizationService(androidApplication()) }

    single {
        DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = {
                androidApplication().dataStoreFile(DataStoreConfig.DATA_STORE_FILE_NAME)
            },
        )
    }
}