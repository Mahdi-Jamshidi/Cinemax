package ir.magiccodes.cinemax.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import ir.magiccodes.cinemax.R
import ir.magiccodes.cinemax.databinding.ItemSearchResultBinding
import ir.magiccodes.cinemax.model.data.MoviePreviewData
import ir.magiccodes.cinemax.util.genreNameById
import ir.magiccodes.cinemax.util.imageUrlProvider
import ir.magiccodes.cinemax.util.loadImage
import ir.magiccodes.cinemax.util.rateStyle

class SearchResultAdapter(private val data: ArrayList<MoviePreviewData>, private val context: Context, private val searchResultEvent: SearchResultEvent) :
    RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {
    lateinit var binding: ItemSearchResultBinding

    inner class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindData(position: Int) {
            binding.tvSearchName.text = data[position].movieName
            binding.tvSearchOriginalName.text = data[position].original_name
            binding.tvSearchDate.text = data[position].releaseDate
            binding.tvSearchLanguage.text = data[position].language

            binding.tvSearchRate.text = rateStyle(data[position].movieRate)

            val genresName = if (data[position].genreIds.isEmpty()) "no data" else genreNameById(data[position].genreIds[0], context)
            binding.tvSearchGenres.text = genresName

            loadImage(imageUrlProvider(data[position].posterUrl), binding.imgMovie,R.drawable.ic_no_image)

            itemView.setOnClickListener {
                searchResultEvent.onItemClicked(data[position].movieId)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemSearchResultBinding.inflate(layoutInflater, parent, false)
        return SearchResultViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
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

    @SuppressLint("NotifyDataSetChanged")
    fun removeList(){
        data.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addNewData(newData: List<MoviePreviewData>){
        data.addAll(newData)
        notifyDataSetChanged()
    }

    interface SearchResultEvent {
        fun onItemClicked(movieId: Int)
    }
}