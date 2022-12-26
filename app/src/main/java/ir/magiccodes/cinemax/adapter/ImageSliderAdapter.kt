package ir.magiccodes.cinemax.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import ir.magiccodes.cinemax.databinding.ImageContainerBinding
import ir.magiccodes.cinemax.model.data.MoviePreviewData
import ir.magiccodes.cinemax.util.dateStyle
import ir.magiccodes.cinemax.util.imageUrlProvider
import ir.magiccodes.cinemax.util.loadImage

class ImageSliderAdapter(
    private val data: List<MoviePreviewData>,
    private val viewPager2: ViewPager2,
    private val sliderEvent: SliderEvent
) :
    RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {
    lateinit var binding: ImageContainerBinding

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(position: Int) {

            binding.tvSliderMovieName.text = data[position].movieName
            binding.tvSliderReleaseDate.text = dateStyle(data[position].releaseDate)

            loadImage(imageUrlProvider(data[position].backdropUrl) , binding.imageView ,null)

            itemView.setOnClickListener {
                sliderEvent.onSliderClicked(data[position].movieId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ImageContainerBinding.inflate(layoutInflater, parent, false)
        return ImageViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return 6
    }

    interface SliderEvent{
       fun onSliderClicked(movieId: Int)
    }

}

