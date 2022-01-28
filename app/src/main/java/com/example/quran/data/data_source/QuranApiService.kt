package com.example.quran.data.data_source

import com.example.quran.domain.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QuranApiService {

    @GET("chapters")
    suspend fun getChapters(@Query("language") language: String = "en"): Response<Chapters>

    @GET("verses/by_chapter/{chapter_number}")
    suspend fun getVerses(
        @Path("chapter_number") chapterNumber: Int,
        @Query("page") page: Int,
        @Query("language") language: String = "en",
        @Query("words") words: Boolean = false,
        @Query("audio") audio: Int = 1,
        @Query("per_page") perPage: Int = 50,
        @Query("fields") fields: String = "text_uthmani",
        @Query("translations") translations: String = "20,131"
    ): Response<Verses>

    @GET("resources/chapter_reciters")
    suspend fun getChapterReciters(
        @Query("language") language: String = "en"
    ): Response<List<Reciters>>

}