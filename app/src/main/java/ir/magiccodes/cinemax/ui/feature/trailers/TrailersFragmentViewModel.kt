package ir.magiccodes.cinemax.ui.feature.trailers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.magiccodes.cinemax.model.data.MoviePreviewData
import ir.magiccodes.cinemax.model.data.VideoResponse
import ir.magiccodes.cinemax.model.repository.Repository
import ir.magiccodes.cinemax.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class TrailersFragmentViewModel(
    private val repository: Repository
) : ViewModel() {

    val movieData = MutableLiveData<List<MoviePreviewData>>()
    val dataVideo = MutableLiveData<VideoResponse>()

    fun getMovie(genreId: String){
        viewModelScope.launch(coroutineExceptionHandler) {
            movieData.value = repository.getMovieByGenre(genreId).shuffled()
        }
    }

    fun getVideo(movieId: Int){
        viewModelScope.launch(coroutineExceptionHandler) {
            dataVideo.value = repository.getVideo(movieId)
        }
    }
}