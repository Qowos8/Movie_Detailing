package com.example.homework1.domain.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class cast(
    @SerialName("id")
    val id: Int,
    @SerialName("cast")
    val cast: List<Actor>

)
@Serializable
data class Actor(
    @SerialName("id")
    val id: Int,
    @SerialName("original_name")
    val name: String,
    @SerialName("character")
    val role: String,
    @SerialName("profile_path")
    val profile_path: String,
    @SerialName("cast_id")
    val cast_id: Int,
    @SerialName("order")
    val order: Int
) {
}
