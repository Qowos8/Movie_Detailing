package com.example.homework1.domain.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieList(
    val movies: List<Movie>
){        constructor() : this(emptyList())

}
@Serializable
data class Movie(
    @SerialName("original_title")
    val name: String,
    val image: String,
    val date: String,
    val rating: Float,
    @SerialName("id")
    val id: Int
) {
    constructor() : this("", "", "", 0.0f, 0)

}