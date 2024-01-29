package com.example.homework1.domain.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MovieImage(
    val aspectRatio: Double?,
    val height: Int?,
    val iso6391: String?,
    val filePath: String?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val width: Int?
)
