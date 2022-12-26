package ir.magiccodes.cinemax.ui.feature.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.magiccodes.cinemax.model.data.CastAndCrewResponse
import ir.magiccodes.cinemax.model.data.MovieDetailData
import ir.magiccodes.cinemax.model.data.MoviePreviewData
import ir.magiccodes.cinemax.model.data.VideoResponse
import ir.magiccodes.cinemax.model.repository.Repository
import ir.magiccodes.cinemax.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class DetailFragmentViewModel(
    private val repository: Repository
) : ViewModel() {

    val dataDetail = MutableLiveData<MovieDetailData>()
    val dataCastAndCrew = MutableLiveData<CastAndCrewResponse>()
    val dataRecommendations = MutableLiveData<List<MoviePreviewData>>()
    val dataVideo = MutableLiveData<VideoResponse>()
    val movieLiked = MutableLiveData(false)

    fun loadData(movieId: Int) {
        getDetail(movieId)
        getCastAndCrew(movieId)
        getRecommendationMovie(movieId)
        getVideo(movieId)
        checkMovieInventory(movieId)
    }

    private fun getDetail(movieId: Int) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val data = repository.getMovieDetail(movieId)
            Log.v("like_view model", movieLiked.toString())
            dataDetail.value = data
        }
    }

    private fun getCastAndCrew(movieId: Int) {
        viewModelScope.launch(coroutineExceptionHandler) {
            dataCastAndCrew.value = repository.getCastAndCrew(movieId)
        }
    }

    private fun getRecommendationMovie(movieId: Int){
        viewModelScope.launch(coroutineExceptionHandler) {
            dataRecommendations.value = repository.getRecommendationsMovie(movieId)
        }
    }

    private fun getVideo(movieId: Int){
        viewModelScope.launch(coroutineExceptionHandler) {
            dataVideo.value = repository.getVideo(movieId)
        }
    }

    fun saveMovieInWishlist(){
        viewModelScope.launch(coroutineExceptionHandler) {
            repository.saveMovieInWishlist()
        }
    }

    fun deleteMovieFromWishlist(){
        viewModelScope.launch(coroutineExceptionHandler) {
            repository.deleteMovieFromWishlist()
        }
    }

    fun checkMovieInventory(movieId: Int){
        //Checking if the movie exists in the database or not
        viewModelScope.launch(coroutineExceptionHandler) {
            if (repository.getMovieById(movieId) == null){
                movieLiked.value = false
            } else {
                movieLiked.value = true
            }
        }
    }

}