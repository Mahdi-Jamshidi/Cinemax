package ir.magiccodes.cinemax.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import ir.magiccodes.cinemax.R
import ir.magiccodes.cinemax.databinding.ItemMoviePreviewBinding
import ir.magiccodes.cinemax.databinding.ItemSearchResultBinding
import ir.magiccodes.cinemax.model.data.MoviePreviewData
import ir.magiccodes.cinemax.util.genreNameById
import ir.magiccodes.cinemax.util.imageUrlProvider
import ir.magiccodes.cinemax.util.loadImage
import ir.magiccodes.cinemax.util.rateStyle

class MoviePreviewAdapter(private val data: List<MoviePreviewData>, private val context: Context, private val moviePreviewEvent: MoviePreviewEvent) :
    RecyclerView.Adapter<MoviePreviewAdapter.MoviePosterViewHolder>() {
    lateinit var binding: ItemMoviePreviewBinding

    inner class MoviePosterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindData(position: Int) {
            binding.tvMovieName.text = data[position].movieName
            binding.tvMovieRate.text = rateStyle(data[position].movieRate)

            val genresName = if (data[position].genreIds.isEmpty()) "no data" else genreNameById(data[position].genreIds[0], context)
            binding.tvMovieGenres.text = genresName

            loadImage(imageUrlProvider(data[position].posterUrl), binding.imgMovie,R.drawable.ic_no_image)

            binding.cardView.setOnClickListener {
                moviePreviewEvent.onPreviewClicked(data[position].movieId)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePosterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemMoviePreviewBinding.inflate(layoutInflater, parent, false)
        return MoviePosterViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MoviePosterViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    interface MoviePreviewEvent {
        fun onPreviewClicked(movieId: Int)
    }
}