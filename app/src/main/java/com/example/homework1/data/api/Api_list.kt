package com.example.homework1.data.api

import com.example.homework1.data.MovieEntity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Api_list(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val movies: List<Api_movie>
){

}
@Serializable
@Entity(tableName = "ListMovie")
data class Api_movie(
    @SerializedName("adult")
    @ColumnInfo(name = "adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    val backdrop_path: String,
    //@SerializedName("genre_ids")
    //@ColumnInfo(name = "genre_ids")
//    val genre_ids: List<Int>,
    @SerializedName("id")
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) val id: Int,
    @SerializedName("original_language")
    @ColumnInfo(name = "original_language")
    val original_language: String,
    @SerializedName("original_title")
    @ColumnInfo(name = "original_title")
    val original_title: String,
    @SerializedName("overview")
    @ColumnInfo(name = "overview")
    val overview: String,
    @SerializedName("popularity")
    @ColumnInfo(name = "popularity")
    val popularity: Double,
    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    val poster_path: String,
    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    val release_date: String,
    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String,
    @SerializedName("video")
    @ColumnInfo(name = "video")
    val video: Boolean,
    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    val vote_average: Double,
    @SerializedName("vote_count")
    @ColumnInfo(name = "vote_count")
    val vote_count: Int
)

fun Api_movie.toMovieEntity(): MovieEntity = MovieEntity(
        id = this.id,
        title = this.title,
        poster = this.poster_path,
        rate = this.vote_count,
        release_date = this.release_date
    )
