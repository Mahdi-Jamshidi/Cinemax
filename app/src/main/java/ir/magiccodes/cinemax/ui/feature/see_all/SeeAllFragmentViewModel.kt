package ir.magiccodes.cinemax.ui.feature.see_all

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.magiccodes.cinemax.model.data.MoviePreviewData
import ir.magiccodes.cinemax.model.repository.Repository
import ir.magiccodes.cinemax.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class SeeAllFragmentViewModel(
    private val getMovieListRepository: Repository
) : ViewModel() {

    var moreMovieData = MutableLiveData<List<MoviePreviewData>>()
    var showAnimation = MutableLiveData(false)

    fun getUpcoming(pageNum: Int) {
         viewModelScope.launch(coroutineExceptionHandler) {
             val request = launch { moreMovieData.value = getMovieListRepository.getUpcomingMovie(pageNum) }
            request.join()
             showAnimation.value = false
        }
    }

    fun getMoreTrending(pageNum: Int) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val request = launch {  moreMovieData.value = getMovieListRepository.getTrendOfDayList(pageNum) }
            request.join()
            showAnimation.value = false
        }
    }

    fun getMoreFreeToWatch(pageNum: Int) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val request = launch {  moreMovieData.value = getMovieListRepository.getFreeToWatchMovie(pageNum) }
            request.join()
            showAnimation.value = false
        }
    }

}