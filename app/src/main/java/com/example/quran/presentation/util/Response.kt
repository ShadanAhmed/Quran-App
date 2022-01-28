package com.example.quran.presentation.util

sealed class Response<T>(
    val data: T? = null,
    val message: String? = null,
    val loading: Boolean = false
) {
    class Success<T>(data: T): Response<T>(
        data = data,
        loading = false
    )

    class Error<T>(message: String?, data: T? = null): Response<T>(
        message = message,
        data = data,
        loading = false
    )

    class Loading<T>(loading: Boolean): Response<T>(
        loading = loading
    )
}
