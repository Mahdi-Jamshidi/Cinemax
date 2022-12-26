package ir.magiccodes.cinemax.ui.feature.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.magiccodes.cinemax.model.data.MoviePreviewData
import ir.magiccodes.cinemax.model.repository.Repository
import ir.magiccodes.cinemax.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class SearchFragmentViewModel(
    private val repository: Repository
):ViewModel() {

    val searchResult = MutableLiveData<List<MoviePreviewData>>()

    fun searchMovie(movieName: String){

        viewModelScope.launch(coroutineExceptionHandler) {
            searchResult.value = repository.searchMovie(movieName)
        }
    }



}