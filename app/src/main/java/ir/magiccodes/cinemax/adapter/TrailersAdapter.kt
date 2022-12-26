package ir.magiccodes.cinemax.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.magiccodes.cinemax.databinding.ItemTrailerBinding
import ir.magiccodes.cinemax.model.data.MoviePreviewData
import ir.magiccodes.cinemax.util.dateStyle
import ir.magiccodes.cinemax.util.genreNameById
import ir.magiccodes.cinemax.util.imageUrlProvider
import ir.magiccodes.cinemax.util.loadImage

class TrailersAdapter(private val data:ArrayList<MoviePreviewData>, private val trailersEvent: TrailersEvent): RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder>() {
    lateinit var binding: ItemTrailerBinding

    inner class TrailersViewHolder(itemView: View,private val context:Context):RecyclerView.ViewHolder(itemView){

        fun bindData(position: Int){
            binding.movieName.text = data[position].movieName
            binding.movieDate.text = dateStyle(data[position].releaseDate)
            binding.movieGenre.text = genreNameById(data[position].genreIds[0], context)

            loadImage(imageUrlProvider(data[position].backdropUrl),binding.imgMove,null)

            itemView.setOnClickListener {
                trailersEvent.onMovieClicked(data[position].movieId)
            }

            binding.btnPlay.setOnClickListener {
                trailersEvent.onPlayClicked(data[position].movieId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding= ItemTrailerBinding.inflate(layoutInflater, parent, false)
        return TrailersViewHolder(binding.root, parent.context)
    }

    override fun onBindViewHolder(holder: TrailersViewHolder, position: Int) {
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

    interface TrailersEvent{
        fun onPlayClicked(movieId: Int)
        fun onMovieClicked(movieId: Int)
    }
}