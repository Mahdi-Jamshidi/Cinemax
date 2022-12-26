package ir.magiccodes.cinemax.ui.feature.trailers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.magiccodes.cinemax.R
import ir.magiccodes.cinemax.adapter.TrailersAdapter
import ir.magiccodes.cinemax.databinding.FragmentTrailersBinding
import ir.magiccodes.cinemax.model.data.MoviePreviewData
import ir.magiccodes.cinemax.ui.feature.detail.DetailFragment
import ir.magiccodes.cinemax.util.playTrailer
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrailersFragment : Fragment(), TrailersAdapter.TrailersEvent {
    lateinit var binding: FragmentTrailersBinding
    private val viewModel: TrailersFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrailersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMovie("")
        genreSelected()

        viewModel.movieData.observe(requireActivity()) {
            val adapter = TrailersAdapter(it as ArrayList<MoviePreviewData>, this)

            binding.recyclerTrailersMovie.adapter = adapter
            adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            binding.recyclerTrailersMovie.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }

    }

    override fun onPlayClicked(movieId: Int) {
        viewModel.getVideo(movieId)
        viewModel.dataVideo.observe(requireActivity()) {
            playTrailer(it, requireContext())
        }
    }

    override fun onMovieClicked(movieId: Int) {

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, DetailFragment(movieId))
        transaction.addToBackStack(null)
        transaction.commit()

    }

    private fun genreSelected() {
        var genreId = ""
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {

                R.id.btn_radio_all -> {
                    viewModel.getMovie("")
                }

                R.id.btn_radio_Animation -> {
                    viewModel.getMovie("16")
                }

                R.id.btn_radio_action -> {
                    viewModel.getMovie("28")

                }

                R.id.btn_radio_drama -> {
                    viewModel.getMovie("18")
                }

                R.id.btn_radio_fantasy -> {
                    viewModel.getMovie("14")
                }

                R.id.btn_radio_family -> {
                    viewModel.getMovie("10751")
                }

                R.id.btn_radio_romance -> {
                    viewModel.getMovie("10749")
                }
            }
        }
    }
}