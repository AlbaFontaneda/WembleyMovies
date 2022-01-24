package com.example.wembleymoviesapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "status_message")
    val message: String?,
    @Json(name ="errors")
    val errors: List<String>?,
    @Json(name ="status_code")
    val statusCode: Int?,
    @Json(name ="success")
    val success: Boolean?
): Serializable