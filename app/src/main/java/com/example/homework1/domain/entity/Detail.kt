package com.example.homework1.domain.entity

data class Detail(
    val id: Int,
    val name: String,
    val poster: String,
    val genre: String,
    val rate: Double,
    val review: String,
    val storyLine: String,
    val cast: List<Actor>,
    val cast_name: List<String>,
    val age_rate: String
){}

