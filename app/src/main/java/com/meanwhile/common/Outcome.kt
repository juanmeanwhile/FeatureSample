package com.meanwhile.common

sealed class Outcome<T> {
    data class Progress<T>(val loading: Boolean, val partialData: T? = null) : Outcome<T>()
    data class Success<T>(val data: T) : Outcome<T>()
    data class Failure<T>(val e: Throwable) : Outcome<T>()

    companion object {
        fun <T> loading(isLoading: Boolean = true, partialData: T? = null): Outcome<T> = Progress(isLoading, partialData)

        fun <T> success(data: T): Outcome<T> = Success(data)

        fun <T> failure(e: Throwable): Outcome<T> = Failure(e)
    }
}

fun <T, H> Outcome<T>.mapData(block: (input: T) -> H): Outcome<H> {
    return when (this) {
        is Outcome.Success -> Outcome.success(block.invoke(data))
        is Outcome.Failure -> Outcome.failure(e)
        is Outcome.Progress -> Outcome.loading(loading, partialData?.let { block.invoke(it) })
    }
}