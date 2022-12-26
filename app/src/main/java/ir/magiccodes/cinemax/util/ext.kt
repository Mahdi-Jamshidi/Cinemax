package ir.magiccodes.cinemax.util

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import ir.magiccodes.cinemax.R
import ir.magiccodes.cinemax.databinding.FragmentPlayingBinding
import ir.magiccodes.cinemax.model.data.GenreId
import ir.magiccodes.cinemax.model.data.VideoResponse
import ir.magiccodes.cinemax.ui.feature.detail.DetailFragment
import kotlinx.coroutines.CoroutineExceptionHandler


val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->

    Log.v("error_coroutine", "Error -> " + throwable.message)
}

fun imageUrlProvider(url: String?): String {
    return BASE_IMAGE_URL + url
}

fun genreNameById(id: Int? = 0, context: Context): String {

    val fileInString = context.applicationContext.assets
        .open("genres_id.json")
        .bufferedReader().use { it.readText() }

    val gson = Gson()
    val dataAllGenres = gson.fromJson(fileInString, GenreId::class.java)
    val genreNameMap = mutableMapOf<Int, String>()

    dataAllGenres.genres.forEach {
        genreNameMap[it.id] = it.name
    }
    return genreNameMap[id] ?: ""
}

fun dateStyle(oldDate: String): String {
    var day = oldDate.subSequence(8, 10).toString()
    val month = oldDate.subSequence(5, 7).toString()
    val year = oldDate.subSequence(0, 4).toString()

    var monthName = ""
    when (month) {
        "01" -> {
            monthName = "On January"
        }
        "02" -> {
            monthName = "On February"
        }
        "03" -> {
            monthName = "On March"
        }
        "04" -> {
            monthName = "On April"
        }
        "05" -> {
            monthName = "On May"
        }
        "06" -> {
            monthName = "On June"
        }
        "07" -> {
            monthName = "On July"
        }
        "08" -> {
            monthName = "On August"
        }
        "09" -> {
            monthName = "On September"
        }
        "10" -> {
            monthName = "On October"
        }
        "11" -> {
            monthName = "On November"
        }
        "12" -> {
            monthName = "On December"
        }
    }
    if (day.subSequence(0, 1) == "0") {
        day = day.removeRange(0, 1)
    }
    return "$monthName $day, $year"
}

fun rateStyle(oldRate: Double): String {
    return String.format("%.1f", oldRate).toString()
}

fun loadImage(url: String?, view: ImageView,error: Any? ) {
    Glide.with(view.context)
        .load(url)
        .error(error)
        .into(view)
}


 fun playTrailer(data: VideoResponse, context: Context) {
    if (data.results.isEmpty()) {
        Toast.makeText(
            context,
            "Sorry can't play this Trailer",
            Toast.LENGTH_SHORT
        ).show()
    } else {
        val random = (0 until data.results.size).random()
        val trailerKey = data.results[random].key
        createDialog(trailerKey, context)
    }
}
private fun createDialog(trailerKey: String, context: Context) {
    val dialog = AlertDialog.Builder(context)
    val layoutInflater = LayoutInflater.from(context)
    val dialogBinding = FragmentPlayingBinding.inflate(layoutInflater)
    dialog.setView(dialogBinding.root)

    val youTubePlayerView = dialogBinding.youtubePlayerView
    youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: YouTubePlayer) {
            youTubePlayer.loadVideo(trailerKey, 0F)
        }
    })

    dialog.create()
    dialog.show()
}

