package com.example.quran.data.repository

import com.example.quran.data.data_source.QuranApiService
import com.example.quran.domain.model.*
import com.example.quran.domain.repository.QuranRepository
import retrofit2.Response

class QuranRepositoryImpl(
    private val quranApiService: QuranApiService
): QuranRepository {
    override suspend fun getChapters(): Response<Chapters> {
        return quranApiService.getChapters()
    }

    override suspend fun getVerses(chapterId: Int, reciterId: Int, pageNumber: Int): Response<Verses> {
        return quranApiService.getVerses(chapterNumber = chapterId, audio = reciterId, page = pageNumber)
    }

    override suspend fun getGetChapterReciters(): Response<List<Reciters>> {
        return quranApiService.getChapterReciters()
    }
}