package com.example.quran.domain.use_case

import com.example.quran.domain.model.Reciters
import com.example.quran.domain.repository.QuranRepository
import retrofit2.Response

class GetReciters(
    private val repository: QuranRepository
) {

    suspend operator fun invoke(): Response<List<Reciters>> {
        return repository.getGetChapterReciters()
    }
}