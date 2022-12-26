package ir.magiccodes.cinemax.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoviePreviewData(

    val movieId: Int,
    val movieName: String,
    val original_name:String,
    val movieRate: Double,
    val releaseDate: String,
    val genreIds: List<Int?>,
    val posterUrl: String? = "",
    val backdropUrl: String? = "",
    val language: String
):Parcelable