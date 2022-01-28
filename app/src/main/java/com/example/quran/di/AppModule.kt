package com.example.quran.di

import com.example.quran.data.data_source.QuranApiService
import com.example.quran.data.repository.QuranRepositoryImpl
import com.example.quran.domain.repository.QuranRepository
import com.example.quran.domain.use_case.GetChapters
import com.example.quran.domain.use_case.GetVerses
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideQuranApi(): QuranApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.quran.com/api/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(QuranApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideQuranRepository(apiService: QuranApiService): QuranRepository {
        return QuranRepositoryImpl(apiService)
    }

    @Singleton
    @Provides
    fun provideChapterUseCase(repository: QuranRepository): GetChapters {
        return GetChapters(repository)
    }

    @Singleton
    @Provides
    fun provideVerseUseCase(repository: QuranRepository): GetVerses {
        return GetVerses(repository)
    }
}