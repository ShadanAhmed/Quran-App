package com.example.quran.domain.model

data class Verses(
    val verses: List<Verse>,
    val pagination: Pagination
)

data class Pagination(
    val current_page: Int,
    val next_page: Int?,
    val per_page: Int,
    val total_pages: Int,
    val total_records: Int
)

data class Verse(
    val audio: Audio,
    val id: Int,
    val page_number: Int,
    val verse_number: Int,
    val text_uthmani: String,
    val verse_key: String,
    val translations: List<Translation>
)

data class Translation (
    val resource_id: Int,
    val text: String
)

data class Audio(
    val url: String
)

data class Word(
    val text: String
)