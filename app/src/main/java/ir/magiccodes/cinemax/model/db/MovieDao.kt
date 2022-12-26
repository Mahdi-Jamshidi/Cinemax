package ir.magiccodes.cinemax.model.db

import androidx.room.*
import ir.magiccodes.cinemax.model.data.MovieDetailData

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(movie: MovieDetailData)

    @Delete
    suspend fun deleteMovieFromWishlist(movie: MovieDetailData)

    @Query("SELECT * FROM detail_table ORDER BY saveTime DESC")
    suspend fun getWishlist(): List<MovieDetailData>

    @Query("SELECT * FROM detail_table WHERE movieId = :movieId")
    suspend fun getDetailById(movieId: Int): MovieDetailData?
}