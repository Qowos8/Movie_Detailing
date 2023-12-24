package com.example.homework1.data

import com.example.homework1.data.api.Api_details
import com.example.homework1.data.api.Api_movie
import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Dao
interface MovieDao{
    @Query("SELECT * FROM ${DB.List_columns.TABLE_NAME}")
    fun insertList(): List<Api_movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun getList(movies: List<Api_movie>)
}
@Dao
interface DetailDao{
    @Query("SELECT * FROM ${DB.Detail_columns.TABLE_NAME}")
    fun insertDetail(): List<Api_details>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun getDetail(details: List<Api_details>)
}
@Database(entities = [Api_movie::class], version = 3, exportSchema = false)
abstract class ListDatabase : RoomDatabase() {
    abstract fun ListDao(): MovieDao

    companion object {
        private var instance: ListDatabase? = null

        @Synchronized
        fun newInstance(context: Context): ListDatabase {
            /*val MIGRATION_1_2 = object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("CREATE TABLE ${DB.List_columns.TABLE_NAME} (`id` INTEGER, `release_date` TEXT," +
                            "`title` TEXT, `poster` TEXT, `rate` INTEGER" +
                            "PRIMARY KEY(`id`))")
                }
            }*/

                    return Room.databaseBuilder(context, ListDatabase::class.java,
                        DB.List_columns.TABLE_NAME
                    )
                        .fallbackToDestructiveMigration() // Добавьте эту строку
                        .build()
            }
        }
    }



@Database(entities = [Api_details::class], version = 2, exportSchema = false)
abstract class DetailDataBase : RoomDatabase(){

    abstract fun DetailsDao(): DetailDao
    companion object{
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE ${DB.Detail_columns.TABLE_NAME} (`id` INTEGER, `age_rate` TEXT," +
                            "`title` TEXT, `poster` TEXT, `rate` INTEGER, storyline TEXT" +
                            "PRIMARY KEY(`id`))"
                )
            }
        }
        fun newInstance(applicationContext: Context): DetailDataBase = Room.databaseBuilder(applicationContext, DetailDataBase::class.java,
            DB.List_columns.TABLE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
        }
    }

