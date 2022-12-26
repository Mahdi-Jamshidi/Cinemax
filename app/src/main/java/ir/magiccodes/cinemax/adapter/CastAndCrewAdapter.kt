package ir.magiccodes.cinemax.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.magiccodes.cinemax.databinding.ItemCastAndCrewBinding
import ir.magiccodes.cinemax.model.data.CastAndCrewResponse
import ir.magiccodes.cinemax.util.imageUrlProvider
import ir.magiccodes.cinemax.util.loadImage

class CastAndCrewAdapter(private val data: CastAndCrewResponse):RecyclerView.Adapter<CastAndCrewAdapter.CastAndCrewViewHolder>() {
    lateinit var binding: ItemCastAndCrewBinding

    inner class CastAndCrewViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        fun bindDate(position: Int){

            binding.tvNameCastOrCrew.text = data.cast[position].name
            binding.tvCharacterName.text = data.cast[position].character

            loadImage(imageUrlProvider(data.cast[position].profilePath), binding.imgCastOrCrew, null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastAndCrewViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemCastAndCrewBinding.inflate(layoutInflater, parent, false)
        return CastAndCrewViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CastAndCrewViewHolder, position: Int) {
        holder.bindDate(position)
    }

    override fun getItemCount(): Int {
        return if (data.cast.size > 10){
            10
        }else{
            data.cast.size
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}