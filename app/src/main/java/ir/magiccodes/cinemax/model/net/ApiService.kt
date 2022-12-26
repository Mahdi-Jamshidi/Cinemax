package ir.magiccodes.cinemax.model.net

import ir.magiccodes.cinemax.model.data.*
import ir.magiccodes.cinemax.util.API_KEY
import ir.magiccodes.cinemax.util.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/upcoming")
    suspend fun getUpcomingMovie(
        @Query("api_key") api_key: String = API_KEY,
        @Query("page") page_num: Int = 1
    ): MovieResponse

    @GET("movie/popular")
    suspend fun getPopularMovie(
        @Query("api_key") api_key: String = API_KEY,
        @Query("page") page_num: Int = 1
    ): MovieResponse

    @GET("trending/movie/day")
    suspend fun getTrendOfDayMovieList(
        @Query("api_key") api_key: String = API_KEY,
        @Query("page") page_num: Int = 1
    ): MovieResponse

    @GET("discover/movie")
    suspend fun getFreeToWatchMovie(
        @Query("api_key") api_key: String = API_KEY,
        @Query("sort_by") sort_by: String = "vote_average.desc",
        @Query("page") page_num: Int = 1,
        @Query("vote_count.gte") vote_count: Int = 200,
        @Query("with_watch_monetization_types") watch_type:String = "free"
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") id_movie: Int ,
        @Query("api_key") api_key: String = API_KEY
    ): DetailResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getCredits(
        @Path("movie_id") id_movie: Int ,
        @Query("api_key") api_key: String = API_KEY
    ): CastAndCrewResponse

    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendationsMovie(
        @Path("movie_id") id_movie: Int ,
        @Query("api_key") api_key: String = API_KEY,
        @Query("page") page_num: Int = 1
    ):MovieResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getVideo(
        @Path("movie_id") id_movie: Int ,
        @Query("api_key") api_key: String = API_KEY,
    ): VideoResponse

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") api_key: String = API_KEY,
        @Query("query") movieName: String,
        @Query("page") page_num: Int = 1
    ):MovieResponse

    @GET("discover/movie")
    suspend fun getMovieByGenre(
        @Query("api_key") api_key: String = API_KEY,
        @Query("with_genres") genreId: String
    ):MovieResponse
}

fun createApiService(): ApiService {

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(ApiService::class.java)
}