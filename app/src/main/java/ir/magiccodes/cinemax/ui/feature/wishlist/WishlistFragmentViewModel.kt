package ir.magiccodes.cinemax.ui.feature.wishlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.magiccodes.cinemax.model.data.MovieDetailData
import ir.magiccodes.cinemax.model.db.MovieDao
import ir.magiccodes.cinemax.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class WishlistFragmentViewModel(
    private val movieDao: MovieDao
): ViewModel() {

    val dataWishlist = MutableLiveData<List<MovieDetailData>>()

    init {
        getAllDataFromDatabase()
    }

    private fun getAllDataFromDatabase(){
        viewModelScope.launch(coroutineExceptionHandler) {
            dataWishlist.value = movieDao.getWishlist()
        }
    }

    fun deleteMovieFromDatabase(movie: MovieDetailData){
        viewModelScope.launch(coroutineExceptionHandler) {
            movieDao.deleteMovieFromWishlist(movie)
        }
    }
}