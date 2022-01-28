package com.example.quran.domain.repository

import com.example.quran.data.data_source.QuranApiService
import com.example.quran.domain.model.*
import retrofit2.Response
import javax.inject.Inject

interface QuranRepository {

    suspend fun getChapters(): Response<Chapters>

    suspend fun getVerses(chapterId: Int, reciterId: Int, pageNumber: Int): Response<Verses>

    suspend fun getGetChapterReciters(): Response<List<Reciters>>

}