package ir.magiccodes.cinemax.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detail_table")
data class MovieDetailData(

    @PrimaryKey
    val movieId: Int,

    val movieName: String,
    val moviePoster: String,
    val movieCalender: String,
    val movieRunTime: String,
    val movieGenre: String,
    val movieRate: String,
    val movieHomePage: String,
    val movieOverview: String,
    val saveTime: Long = 0
)