package com.example.wembleymoviesapp.data

import com.example.wembleymoviesapp.data.model.ErrorResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class NetworkHelper {

    suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T
    ): RequestResponse<T> {
        return withContext(dispatcher) {
            try {
                RequestResponse.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> RequestResponse.NetworkError
                    is HttpException -> {
                        convertErrorBody(throwable)?.let {
                            RequestResponse.GenericError(it.statusCode, it.message ?: it.errors?.firstOrNull())
                        } ?: run { RequestResponse.GenericError(null, "Unknow error") }
                    }
                    else -> {
                        RequestResponse.GenericError(null, "Unknow error")
                    }
                }
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
        return try {
            throwable.response()?.errorBody()?.source()?.let {
                val moshi: Moshi = Moshi.Builder()
                    .addLast(KotlinJsonAdapterFactory())
                    .build()
                val moshiAdapter: JsonAdapter<ErrorResponse> = moshi.adapter(ErrorResponse::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }
}

sealed class RequestResponse<out T> {
    data class Success<out R>(val value: R): RequestResponse<R>()
    data class GenericError(val code: Int? = null, val error: String? = null): RequestResponse<Nothing>()
    object NetworkError: RequestResponse<Nothing>()
}