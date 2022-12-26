package ir.magiccodes.cinemax.ui.feature.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.magiccodes.cinemax.model.data.MoviePreviewData
import ir.magiccodes.cinemax.model.data.VideoResponse
import ir.magiccodes.cinemax.model.repository.Repository
import ir.magiccodes.cinemax.util.coroutineExceptionHandler
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

class HomeFragmentViewModel(
    private val repository: Repository
) : ViewModel() {

    var upcomingMovie = MutableLiveData<List<MoviePreviewData>>()
    var mostPopularMovie = MutableLiveData<List<MoviePreviewData>>()
    var trendingOfDayMovie = MutableLiveData<List<MoviePreviewData>>()
    var freeToWatchMovie = MutableLiveData<List<MoviePreviewData>>(listOf())
    val dataVideo = MutableLiveData<VideoResponse>()
    var showShimmerAndSwipeRefresh = MutableLiveData(true)

    fun refreshAllDataFromNet() {

        viewModelScope.launch(coroutineExceptionHandler) {
            val request = launch {
                val newDataUpcoming = async { repository.getUpcomingMovie(1) }
                upcomingMovie.value = newDataUpcoming.await().shuffled()


                val newDataPopularMovie = async { repository.getPopularMovie(1) }
                mostPopularMovie.value = newDataPopularMovie.await().shuffled()

                val newDataTrending = async { repository.getTrendOfDayList(1) }
                trendingOfDayMovie.value = newDataTrending.await().shuffled()

                val newDataFreeToWatchMovie = async { repository.getFreeToWatchMovie(1) }
                freeToWatchMovie.value = newDataFreeToWatchMovie.await().shuffled()
                Log.v("logggggggg", freeToWatchMovie.value!!.size.toString())
            }

            request.join()
            showShimmerAndSwipeRefresh.value = false


        }

    }

    fun getVideo(movieId: Int) {
        viewModelScope.launch(coroutineExceptionHandler) {
            dataVideo.value = repository.getVideo(movieId)
        }
    }
}