package com.example.quran.domain.use_case

import com.example.quran.domain.model.Chapter
import com.example.quran.domain.model.Chapters
import com.example.quran.domain.repository.QuranRepository
import retrofit2.Response

class GetChapters(
    private val repository: QuranRepository
) {

    suspend operator fun invoke(): Response<Chapters> {
        return repository.getChapters()
    }
}