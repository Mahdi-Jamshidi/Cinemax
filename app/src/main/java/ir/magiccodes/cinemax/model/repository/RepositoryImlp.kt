package ir.magiccodes.cinemax.model.repository

import android.util.Log
import ir.magiccodes.cinemax.model.data.*
import ir.magiccodes.cinemax.model.db.MovieDao
import ir.magiccodes.cinemax.model.net.ApiService
import ir.magiccodes.cinemax.util.rateStyle

class RepositoryImpl(
    private val apiService: ApiService,
    private val movieDao: MovieDao
) : Repository {

    lateinit var movieDetail: MovieDetailData

    override suspend fun getUpcomingMovie(pageNum: Int): List<MoviePreviewData> {
        val result = apiService.getUpcomingMovie(page_num = pageNum)
        return getAndFormatData(result)
    }

    override suspend fun getPopularMovie(pageNum: Int): List<MoviePreviewData> {
        val result = apiService.getPopularMovie(page_num = pageNum)
        return getAndFormatData(result)
    }

    override suspend fun getTrendOfDayList(pageNum: Int): List<MoviePreviewData> {
        val result = apiService.getTrendOfDayMovieList(page_num = pageNum)
        return getAndFormatData(result)
    }

    override suspend fun getFreeToWatchMovie(pageNum: Int): List<MoviePreviewData> {
        val result = apiService.getFreeToWatchMovie(page_num = pageNum)
        val newFreeMovieList: ArrayList<MoviePreviewData> = arrayListOf()
        result.results.forEach {
            newFreeMovieList.add(
                MoviePreviewData(
                    it.id,
                    it.title,
                    it.originalTitle,
                    it.voteAverage,
                    it.releaseDate,
                    it.genreIds,
                    it.posterPath,
                    it.backdropPath,
                    it.originalLanguage
                )
            )
        }
        return newFreeMovieList
    }

    override suspend fun getMovieDetail(movieId: Int): MovieDetailData {

        val result = apiService.getMovieDetail(movieId)
        movieDetail = MovieDetailData(
            result.id,
            result.title,
            result.posterPath,
            result.releaseDate,
            result.runtime.toString(),
            result.genres[0].name,
            rateStyle(result.voteAverage),
            result.homepage,
            result.overview
        )

        return movieDetail
    }

    override suspend fun getRecommendationsMovie(movieId: Int): List<MoviePreviewData> {

        val result = apiService.getRecommendationsMovie(movieId)
        return getAndFormatData(result)
    }

    override suspend fun getCastAndCrew(movieId: Int): CastAndCrewResponse {
        return apiService.getCredits(movieId)
    }

    override suspend fun getVideo(movieId: Int): VideoResponse {
        return apiService.getVideo(movieId)
    }

    override suspend fun saveMovieInWishlist() {
        // We save the time to show the time of saving the items in order
        val now = System.currentTimeMillis()

        val dataToSave = MovieDetailData(
            movieDetail.movieId,
            movieDetail.movieName,
            movieDetail.moviePoster,
            movieDetail.movieCalender,
            movieDetail.movieRunTime,
            movieDetail.movieGenre,
            movieDetail.movieRate,
            movieDetail.movieHomePage,
            movieDetail.movieOverview,
            now
        )
        Log.v("testSave", dataToSave.toString() )
        movieDao.insertOrUpdate(dataToSave)
    }


    override suspend fun deleteMovieFromWishlist() {
        movieDao.deleteMovieFromWishlist(movieDetail)
    }

    override suspend fun getMovieById(movieId: Int): MovieDetailData? {
        return movieDao.getDetailById(movieId)
    }

    override suspend fun searchMovie(movieName: String):List<MoviePreviewData>  {

        val result = apiService.searchMovie(movieName = movieName)
        return getAndFormatData(result)
    }

    override suspend fun getMovieByGenre(genreId: String): List<MoviePreviewData> {
        val result = apiService.getMovieByGenre(genreId = genreId)
        return getAndFormatData(result)
    }


    private fun getAndFormatData(result: MovieResponse): List<MoviePreviewData> {

        // this fun get result and convert to MoviePreviewData format
        val newDataList: ArrayList<MoviePreviewData> = arrayListOf()
        result.results.forEach {
            newDataList.add(
                MoviePreviewData(
                    it.id,
                    it.title,
                    it.originalTitle,
                    it.voteAverage,
                    it.releaseDate,
                    it.genreIds,
                    it.posterPath,
                    it.backdropPath,
                    it.originalLanguage
                )
            )
        }
        return newDataList
    }
}