package com.example.quran.domain.model

data class Chapters(
    val chapters: List<Chapter>
)

data class Chapter(
    val id: Int,
    val name_arabic: String,
    val name_simple: String,
    val translated_name: TranslatedName
)

data class TranslatedName(
    val name: String
)