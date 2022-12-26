package ir.magiccodes.cinemax.ui.feature.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.ShimmerFrameLayout
import ir.magiccodes.cinemax.R
import ir.magiccodes.cinemax.adapter.ImageSliderAdapter
import ir.magiccodes.cinemax.adapter.LatestTrailersAdapter
import ir.magiccodes.cinemax.adapter.MoviePreviewAdapter
import ir.magiccodes.cinemax.databinding.FragmentHomeBinding
import ir.magiccodes.cinemax.databinding.LayoutFragmentHomeBinding
import ir.magiccodes.cinemax.model.data.MoviePreviewData
import ir.magiccodes.cinemax.model.data.TrailersData
import ir.magiccodes.cinemax.ui.feature.detail.DetailFragment
import ir.magiccodes.cinemax.ui.feature.see_all.SeeAllFragment
import ir.magiccodes.cinemax.util.*
import me.relex.circleindicator.CircleIndicator3
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class HomeFragment() : Fragment() {
    private lateinit var bindLayout: FragmentHomeBinding
    private lateinit var binding: LayoutFragmentHomeBinding
    private lateinit var viewPager2: ViewPager2
    var handler = Handler(Looper.myLooper()!!)
    private lateinit var adapter: ImageSliderAdapter
    private val runnable = Runnable { viewPager2.currentItem = viewPager2.currentItem + 1 }

    private var upcomingData = listOf<MoviePreviewData>()
    private var popularData = listOf<MoviePreviewData>()
    private var trendingData = listOf<MoviePreviewData>()
    private var freeWatchData = listOf<MoviePreviewData>()

    lateinit var shimmerLayout: ShimmerFrameLayout
    lateinit var dataLayout: LinearLayout
    private val viewModel: HomeFragmentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refreshAllDataFromNet()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindLayout = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding = bindLayout.dataLayoutHome
        shimmerLayout = bindLayout.shimmerLayout
        dataLayout = bindLayout.dataLayout
        return bindLayout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager2 = binding.viewPager2


        shimmerAnimationHandler()

        observeAndSetData()

        clickedOnSeeAlL()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.showShimmerAndSwipeRefresh.value = true
            viewModel.refreshAllDataFromNet()
            viewModel.showShimmerAndSwipeRefresh.observe(requireActivity()) {
                if (it){
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    private fun clickedOnSeeAlL() {
        binding.btnSeeAllUpcoming.setOnClickListener {
            goAndSendDataToSeeAllFragment(upcomingData, UPCOMING)
        }
        binding.btnSeeAllTernding.setOnClickListener {
            goAndSendDataToSeeAllFragment(trendingData, TRENDING)
        }
        binding.btnSeeAllFreeToWatch.setOnClickListener {
            goAndSendDataToSeeAllFragment(freeWatchData, FREE_TO_WATCH)
        }    }

    private fun shimmerAnimationHandler() {

        viewModel.showShimmerAndSwipeRefresh.observe(requireActivity()) {

            if (it) {
                // show shimmer
                // handling shimmer animation
                shimmerLayout.visibility = View.VISIBLE
                shimmerLayout.startShimmerAnimation()
                dataLayout.visibility = View.INVISIBLE

            } else {
                // don't show shimmer
                dataLayout.visibility = View.VISIBLE
                shimmerLayout.visibility = View.INVISIBLE
                shimmerLayout.stopShimmerAnimation()
            }
        }
    }

    private fun observeAndSetData() {
        viewModel.mostPopularMovie.observe(requireActivity()) {
            popularData = it
            imageSlider(it)
            latestTrailer(it)
        }

        viewModel.upcomingMovie.observe(requireActivity()) {
            upcomingData = it
            upcoming(it)
        }

        viewModel.trendingOfDayMovie.observe(requireActivity()) {
            trendingData = it
            trending(it)
        }

        viewModel.freeToWatchMovie.observe(requireActivity()) {
            freeWatchData = it
            freeToWatch(it)
        }
    }

    private fun imageSlider(data: List<MoviePreviewData>) {

        adapter = ImageSliderAdapter(data, viewPager2, object : ImageSliderAdapter.SliderEvent {
            override fun onSliderClicked(movieId: Int) {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, DetailFragment(movieId))
                transaction.addToBackStack(null)
                transaction.commit()
            }
        })

        viewPager2.adapter = adapter
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val indicator: CircleIndicator3 = binding.indicator
        indicator.setViewPager(viewPager2)
        adapter.registerAdapterDataObserver(indicator.adapterDataObserver)

        setUpTransformer()

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (viewPager2.currentItem == 5) {
                    handler.postDelayed({ viewPager2.currentItem = 0 }, 6000)
                } else {
                    handler.postDelayed(runnable, 6000)
                }
            }
        })

    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        viewPager2.setPageTransformer(transformer)
    }

    private fun upcoming(data: List<MoviePreviewData>) {

        val upcomingAdapter = MoviePreviewAdapter(
            data,
            requireActivity(),
            object : MoviePreviewAdapter.MoviePreviewEvent {
                override fun onPreviewClicked(movieId: Int) {
                    goDetailFragment(movieId)
                }
            })

        binding.recyclerUpcoming.adapter = upcomingAdapter
        upcomingAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerUpcoming.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)

    }

    private fun trending(data: List<MoviePreviewData>) {
        val trendingAdapter = MoviePreviewAdapter(
            data,
            requireActivity(),
            object : MoviePreviewAdapter.MoviePreviewEvent {
                override fun onPreviewClicked(movieId: Int) {
                    goDetailFragment(movieId)
                }
            })

        binding.recyclerTrending.adapter = trendingAdapter
        trendingAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerTrending.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
    }

    private fun latestTrailer(data: List<MoviePreviewData>) {

        val trailers: ArrayList<TrailersData> = arrayListOf()
        data.forEach {
            trailers.add(
                TrailersData(
                    it.movieId,
                    it.movieName,
                    it.releaseDate,
                    genreNameById(it.genreIds[0], requireContext()),
                    it.backdropUrl!!
                )
            )
        }


        val latestTrailerAdapter =
            LatestTrailersAdapter(
                trailers.shuffled(),
                object : LatestTrailersAdapter.LatestTrailersEvent {
                    override fun onTrailerClicked(movieId: Int) {
                        viewModel.getVideo(movieId)
                        viewModel.dataVideo.observe(requireActivity()) {
                            playTrailer(it, requireContext())
                        }
                    }
                })

        binding.recyclerLatestTrailer.adapter = latestTrailerAdapter
        binding.recyclerLatestTrailer.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
    }

    private fun freeToWatch(data: List<MoviePreviewData>) {
        val freeToWatchAdapter = MoviePreviewAdapter(
            data,
            requireActivity(),
            object : MoviePreviewAdapter.MoviePreviewEvent {
                override fun onPreviewClicked(movieId: Int) {
                    goDetailFragment(movieId)
                }
            })
        binding.recyclerFreeToWatch.adapter = freeToWatchAdapter
        freeToWatchAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerFreeToWatch.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
    }

    fun goDetailFragment(movieId: Int) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, DetailFragment(movieId))
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onPause() {
        super.onPause()
        dataLayout.visibility = View.VISIBLE
        shimmerLayout.visibility = View.INVISIBLE
        shimmerLayout.stopShimmerAnimation()
        handler.removeCallbacks(runnable)

    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 6000)
    }

    private fun goAndSendDataToSeeAllFragment(movieList: List<Parcelable>, key_title: String) {

        val bundle = Bundle()
        bundle.putParcelableArrayList("data", ArrayList(movieList))
        bundle.putString(KEY_TITLE, key_title)

        val fragment = SeeAllFragment()
        fragment.arguments = bundle

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}