package com.example.homework1.domain.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
@Serializable
data class cast(
    @SerializedName("id")
    val id: Int,
    @SerializedName("cast")
    val cast: List<Actor>

)
@Serializable
data class Actor(
    @SerializedName("id")
    val id: Int,
    @SerializedName("original_name")
    val name: String,
    @SerializedName("character")
    val role: String,
    @SerializedName("profile_path")
    val profile_path: String,
    @SerializedName("cast_id")
    val cast_id: Int,
    @SerializedName("order")
    val order: Int
) {
}
