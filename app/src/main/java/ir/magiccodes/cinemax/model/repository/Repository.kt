package ir.magiccodes.cinemax.model.repository

import ir.magiccodes.cinemax.model.data.*

interface Repository {

    // use in Home Fragment
    suspend fun getUpcomingMovie(pageNum: Int):List<MoviePreviewData>
    suspend fun getPopularMovie(pageNum: Int):List<MoviePreviewData>
    suspend fun getTrendOfDayList(pageNum: Int):List<MoviePreviewData>
    suspend fun getFreeToWatchMovie(pageNum: Int):List<MoviePreviewData>

    // use in Detail Fragment
    suspend fun getMovieDetail(movieId: Int):MovieDetailData
    suspend fun getRecommendationsMovie(movieId: Int):List<MoviePreviewData>
    suspend fun getCastAndCrew(movieId: Int):CastAndCrewResponse
    suspend fun getVideo(movieId: Int):VideoResponse

    suspend fun saveMovieInWishlist()
    suspend fun deleteMovieFromWishlist()
    suspend fun getMovieById(movieId:Int):MovieDetailData?

    // use in Search Fragment
    suspend fun searchMovie(movieName: String):List<MoviePreviewData>

    // use in Trailers Fragment
    suspend fun getMovieByGenre(genreId: String):List<MoviePreviewData>


}