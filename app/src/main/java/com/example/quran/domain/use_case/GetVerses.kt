package com.example.quran.domain.use_case

import com.example.quran.domain.model.Verse
import com.example.quran.domain.model.Verses
import com.example.quran.domain.repository.QuranRepository
import retrofit2.Response

class GetVerses(
    private val repository: QuranRepository
) {
    suspend operator fun invoke(chapterId: Int, reciterId: Int, pageNumber: Int): Response<Verses> {
        return repository.getVerses(chapterId, reciterId, pageNumber)
    }
}