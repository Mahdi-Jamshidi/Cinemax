package ir.magiccodes.cinemax.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.magiccodes.cinemax.databinding.ItemWishlistBinding
import ir.magiccodes.cinemax.model.data.MovieDetailData
import ir.magiccodes.cinemax.util.imageUrlProvider
import ir.magiccodes.cinemax.util.loadImage

class WishlistAdapter(private val data: ArrayList<MovieDetailData>, private val wishlistEvent: WishlistEvent):RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {
    lateinit var binding: ItemWishlistBinding

    inner class WishlistViewHolder(itemView: View, private val context: Context):RecyclerView.ViewHolder(itemView){
        fun bindItem(position: Int){

            binding.tvWishlistGenre.text = data[position].movieGenre
            binding.tvWishlistName.text = data[position].movieName
            binding.tvWishlistRate.text = data[position].movieRate

            loadImage(imageUrlProvider(data[position].moviePoster), binding.imgMovieWishlist, null)

            binding.btnMovieLiked.isChecked = true
            binding.btnMovieLiked.setOnClickListener {
                binding.btnMovieLiked.isChecked = false
                wishlistEvent.onUnLikeClicked(data[position], adapterPosition)
            }

            itemView.setOnClickListener {
                wishlistEvent.onItemClicked(data[position])
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemWishlistBinding.inflate(layoutInflater, parent, false)
        return WishlistViewHolder(binding.root , parent.context)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        holder.bindItem(position)
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

    fun removeMovie(oldMovie: MovieDetailData, position: Int){
        data.remove(oldMovie)
        notifyItemRemoved(position)
    }


    interface WishlistEvent {
        fun onUnLikeClicked(movie: MovieDetailData, position: Int)
        fun onItemClicked(movie: MovieDetailData)
    }
}