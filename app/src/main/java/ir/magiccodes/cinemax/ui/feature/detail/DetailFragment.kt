package ir.magiccodes.cinemax.ui.feature.detail

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.varunest.sparkbutton.SparkEventListener
import ir.magiccodes.cinemax.R
import ir.magiccodes.cinemax.adapter.CastAndCrewAdapter
import ir.magiccodes.cinemax.adapter.RecommendationMovieAdapter
import ir.magiccodes.cinemax.adapter.WishlistAdapter
import ir.magiccodes.cinemax.databinding.FragmentMovieDetailBinding
import ir.magiccodes.cinemax.databinding.FragmentPlayingBinding
import ir.magiccodes.cinemax.util.imageUrlProvider
import ir.magiccodes.cinemax.util.loadImage
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailFragment(private val movieId: Int) : Fragment() {
    lateinit var binding: FragmentMovieDetailBinding
    private val viewModel: DetailFragmentViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMovieDetailBinding.inflate(layoutInflater, container, false)
        return binding.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        viewModel.loadData(movieId)

        setDetailData()
        setCastAndCrew()
        setRecommendationsMovies()
        playTrailer()
        saveMovieInWishlist()

    }

    private fun saveMovieInWishlist() {
        val buttonLike = binding.btnMovieLiked

        viewModel.movieLiked.observe(requireActivity()) { isLiked ->
            if (isLiked) {
                buttonLike.isChecked = true
            } else {
                buttonLike.setEventListener(object : SparkEventListener {
                    override fun onEvent(button: ImageView, buttonState: Boolean) {
                        if (buttonState) {
                            // Button is active
                            viewModel.saveMovieInWishlist()
                        } else {
                            // Button is inactive
                            viewModel.deleteMovieFromWishlist()
                        }
                    }

                    override fun onEventAnimationEnd(button: ImageView?, buttonState: Boolean) {}
                    override fun onEventAnimationStart(button: ImageView?, buttonState: Boolean) {}
                })
            }
        }


    }

    private fun playTrailer() {
        binding.btnPlayTrailer.setOnClickListener {
            viewModel.dataVideo.observe(requireActivity()) {
                ir.magiccodes.cinemax.util.playTrailer(it,requireContext())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDetailData() {

        viewModel.dataDetail.observe(requireActivity()) { data ->
            binding.tvDetailMovieName.text = data.movieName
            binding.tvDetailMovieCalender.text = data.movieCalender
            binding.tvDetailMovieRunTime.text = data.movieRunTime + " Minutes"
            binding.tvDetailMovieGenres.text = data.movieGenre
            binding.tvMovieRate.text = data.movieRate

            if (data.movieOverview == "") {
                binding.tvDetailMovieOverview.text = "Sorry there is no information about this.."
            } else {
                binding.tvDetailMovieOverview.text = data.movieOverview
            }

            loadImage(imageUrlProvider(data.moviePoster), binding.imgDetailPoster, null)
            loadImage(imageUrlProvider(data.moviePoster), binding.imgDetailPosterBig, null)

            binding.btnDetailOpenSite.setOnClickListener {
                if (data.movieHomePage == "") {
                    Toast.makeText(
                        requireContext(),
                        "this movie don't have home website",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.movieHomePage))
                    startActivity(intent)
                }
            }
        }

    }

    private fun setCastAndCrew() {
        viewModel.dataCastAndCrew.observe(requireActivity()) {
            if (it.cast.isEmpty()) {
                binding.tvRecommendation.visibility = View.GONE
                binding.recyclerRecommendations.visibility = View.GONE
            } else {
                val adapter = CastAndCrewAdapter(it)
                binding.recyclerCastAndCrew.adapter = adapter
                binding.recyclerCastAndCrew.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            }
        }

    }

    private fun setRecommendationsMovies() {
        viewModel.dataRecommendations.observe(requireActivity()) {

            if (it.isEmpty()) {
                binding.tvRecommendation.visibility = View.GONE
                binding.recyclerRecommendations.visibility = View.GONE
            } else {
                val adapter = RecommendationMovieAdapter(
                    it,
                    object : RecommendationMovieAdapter.RecommendationEvent {
                        override fun onRecommendationClicked(movieId: Int) {
                            val transaction = parentFragmentManager.beginTransaction()
                            transaction.replace(R.id.fragmentContainerView, DetailFragment(movieId))
                            transaction.addToBackStack(null)
                            transaction.commit()
                        }
                    })

                binding.recyclerRecommendations.adapter = adapter
                binding.recyclerRecommendations.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            }

        }

    }

}