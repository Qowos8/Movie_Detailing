package DB
import android.provider.BaseColumns

object DB {
    const val DB_NAME = "Movie.db"

    object List_columns{
        const val TABLE_NAME = "ListMovie"
        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_POSTER = "poster"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_RELEASE = "release"
        const val COLUMN_NAME_RATE = "rate"
    }

    object Detail_columns{
        const val TABLE_NAME = "DetailMovie"
        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_POSTER = "poster"
        const val COLUMN_NAME_AGE = "age_rate"
        const val COLUMN_NAME_STORYLINE = "storyline"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_RATE = "rate"
    }
}