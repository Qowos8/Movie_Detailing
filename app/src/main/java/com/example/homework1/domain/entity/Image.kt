package com.example.homework1.domain.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MovieImage(
    val aspectRatio: Double?, // Соотношение сторон
    val height: Int?, // Высота изображения
    val iso6391: String?, // Код языка (если применимо)
    val filePath: String?, // Путь к файлу изображения
    val voteAverage: Double?, // Средний рейтинг
    val voteCount: Int?, // Количество голосов
    val width: Int? // Ширина изображения
)
data class ImageResponse(
    @SerializedName("backdrops") val backdrops: List<MovieImage>,
)
