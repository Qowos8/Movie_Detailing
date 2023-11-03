package DB

import Data.Api_details
import Data.Api_movie
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DB.List_columns.TABLE_NAME)
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "release")
    val release_date: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "poster")
    val poster: String,
    @ColumnInfo(name = "rate")
    val rate: Int
)
fun toApiMovie(entity: List<MovieEntity>): List<Api_movie> = listOf(api_movie)



val api_movie = Api_movie(
    id = 1,
    release_date = "release_date",
    title = "title",
    poster_path = "poster_path",
    vote_count = 45,
    adult = true,
    backdrop_path = "",
    //genre_ids = listOf(0,0,0),
    vote_average = 2.0,
    overview = "",
    original_language = "English",
    video = false,
    original_title = "",
    popularity = 1.0
)

@Entity(tableName = DB.Detail_columns.TABLE_NAME)
data class DetailsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "poster")
    val poster: String,
    @ColumnInfo(name = "age_rate")
    val age_rate: String,
    @ColumnInfo(name = "storyline")
    val storyline: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "rate")
    val rate: Float
)
fun toApiDetails(entity: DetailsEntity): Api_details = api_details

val api_details = Api_details(
    id = 1,
    original_title = "",
    poster_path = "",
    vote_average = 1.0,
    adult = "",
    overview = "",
    backdrop_path = "",
    budget = "",
    original_language = "",
    release_data = "",
    runtime = "",
    status = "released"
)




