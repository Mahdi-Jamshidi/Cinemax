package ir.magiccodes.cinemax.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.magiccodes.cinemax.databinding.ItemCastAndCrewBinding
import ir.magiccodes.cinemax.databinding.ItemDetailRecommendationsBinding
import ir.magiccodes.cinemax.model.data.CastAndCrewResponse
import ir.magiccodes.cinemax.model.data.MoviePreviewData
import ir.magiccodes.cinemax.util.imageUrlProvider
import ir.magiccodes.cinemax.util.loadImage

class RecommendationMovieAdapter(private val data: List<MoviePreviewData>, private val recommendationEvent: RecommendationEvent):RecyclerView.Adapter<RecommendationMovieAdapter.RecommendationViewHolder>() {
    lateinit var binding: ItemDetailRecommendationsBinding

    inner class RecommendationViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        fun bindDate(position: Int){
            binding.tvNameRecommendMovie.text = data[position].movieName

            loadImage(imageUrlProvider(data[position].backdropUrl), binding.imgRecommendMovie, null)

            itemView.setOnClickListener {
                recommendationEvent.onRecommendationClicked(data[position].movieId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemDetailRecommendationsBinding.inflate(layoutInflater, parent, false)
        return RecommendationViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bindDate(position)
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

    interface RecommendationEvent{
        fun onRecommendationClicked(movieId: Int)
    }
}