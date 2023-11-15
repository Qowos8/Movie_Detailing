package com.example.homework1.data.` api`

import com.example.homework1.data.DetailsEntity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "DetailMovie")
data class Api_details(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "original_title")
    val original_title: String,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "poster_path")
    val poster_path: String,
    @ColumnInfo(name = "release_data")
    val release_data: String?,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "backdrop_path")
    val backdrop_path: String,
    @ColumnInfo(name = "vote_average")
    val vote_average: Double,
    //val genres: String,
    @ColumnInfo(name = "runtime")
    val runtime: String,
    //val actors: List<Actor>,
    //val directors: List<Actor>,
    @ColumnInfo(name = "budget")
    val budget: String,
    @ColumnInfo(name = "original_language")
    val original_language: String,
    @ColumnInfo(name = "adult")
    val adult: String

)
fun Api_details.toDetailsEntity(): DetailsEntity {
    return DetailsEntity(
        id = this.id,
        poster = this.poster_path,
        rate = this.vote_average.toFloat(),
        age_rate = this.adult,
        title = this.original_title,
        storyline = this.overview
    )
}