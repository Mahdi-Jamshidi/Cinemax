package ir.magiccodes.cinemax.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.magiccodes.cinemax.databinding.ItemLatestTrailerBinding
import ir.magiccodes.cinemax.model.data.TrailersData
import ir.magiccodes.cinemax.util.imageUrlProvider
import ir.magiccodes.cinemax.util.loadImage

class LatestTrailersAdapter(private val data:List<TrailersData>, private val latestTrailersEvent: LatestTrailersEvent): RecyclerView.Adapter<LatestTrailersAdapter.LatestTrailersViewHolder>() {
    lateinit var binding: ItemLatestTrailerBinding

    inner class LatestTrailersViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        fun bindData(position: Int){
            binding.tvMovieNameLatestTrailer.text = data[position].movieName

            loadImage(imageUrlProvider(data[position].backdropImage),binding.imgItemLatest,null)

            itemView.setOnClickListener {
                latestTrailersEvent.onTrailerClicked(data[position].movieId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestTrailersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding= ItemLatestTrailerBinding.inflate(layoutInflater, parent, false)
        return LatestTrailersViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: LatestTrailersViewHolder, position: Int) {
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

    interface LatestTrailersEvent{
        fun onTrailerClicked(movieId: Int)
    }
}