package ir.magiccodes.cinemax.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.magiccodes.cinemax.model.data.MovieDetailData

@Database(entities = [MovieDetailData::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun movieDao(): MovieDao
}