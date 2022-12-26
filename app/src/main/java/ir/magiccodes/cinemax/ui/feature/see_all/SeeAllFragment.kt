package ir.magiccodes.cinemax.ui.feature.see_all

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.magiccodes.cinemax.R
import ir.magiccodes.cinemax.adapter.SearchResultAdapter
import ir.magiccodes.cinemax.databinding.FragmentSeeAllBinding
import ir.magiccodes.cinemax.model.data.MoviePreviewData
import ir.magiccodes.cinemax.ui.feature.detail.DetailFragment
import ir.magiccodes.cinemax.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SeeAllFragment : Fragment(), SearchResultAdapter.SearchResultEvent {
    lateinit var binding: FragmentSeeAllBinding
    var data = ArrayList<MoviePreviewData>()
    lateinit var adapter: SearchResultAdapter
    lateinit var recyclerView: RecyclerView
    private val viewModel: SeeAllFragmentViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSeeAllBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val title = requireArguments().getString(KEY_TITLE)!!
        data = requireArguments().getParcelableArrayList<MoviePreviewData>("data")!!
        recyclerInitial(data)

        binding.tvTitle.text = title


        buttonMore(title)

    }

    private fun buttonMore(title:String) {
        var pageNum = 1
        binding.btnSeeAllMore.setOnClickListener {
            viewModel.showAnimation.value = true
            pageNum += 1
            when (title) {
                UPCOMING -> {
                    viewModel.getUpcoming(pageNum)
                }
                TRENDING -> {
                    viewModel.getMoreTrending(pageNum)
                }
                FREE_TO_WATCH -> {
                    viewModel.getMoreFreeToWatch(pageNum)
                }
            }

            viewModel.showAnimation.observe(requireActivity()){ showAnim ->

                if (showAnim){
                    // show loading animation
                    binding.btnSeeAllMore.visibility = View.GONE
                    binding.lottieLoading.visibility = View.VISIBLE
                    binding.lottieLoading.playAnimation()

                } else {
                    // stop loading animation & set new data to recycler
                    binding.btnSeeAllMore.visibility = View.VISIBLE
                    binding.lottieLoading.visibility = View.GONE
                    binding.lottieLoading.pauseAnimation()

                    viewModel.moreMovieData.observe(requireActivity()) {
                        data.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                    viewModel.moreMovieData.removeObservers(requireActivity())
                }
            }
        }
    }

    private fun recyclerInitial(data: List<MoviePreviewData>) {
        adapter = SearchResultAdapter(data as ArrayList<MoviePreviewData>, requireContext(), this)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(false)

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onItemClicked(movieId: Int) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, DetailFragment(movieId))
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
