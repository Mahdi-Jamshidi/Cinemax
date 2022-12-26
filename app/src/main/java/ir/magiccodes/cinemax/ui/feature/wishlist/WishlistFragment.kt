package ir.magiccodes.cinemax.ui.feature.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.magiccodes.cinemax.R
import ir.magiccodes.cinemax.adapter.WishlistAdapter
import ir.magiccodes.cinemax.databinding.FragmentWishlistBinding
import ir.magiccodes.cinemax.model.data.MovieDetailData
import ir.magiccodes.cinemax.ui.feature.detail.DetailFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class WishlistFragment : Fragment(), WishlistAdapter.WishlistEvent {
    lateinit var binding: FragmentWishlistBinding
    var data = arrayListOf<MovieDetailData>()
    private lateinit var myAdapter: WishlistAdapter

    private val viewModel: WishlistFragmentViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWishlistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialRecycler()

    }

    private fun initialRecycler(){
        viewModel.dataWishlist.observe(requireActivity()) {
            data = ArrayList(it)
            myAdapter = WishlistAdapter(data , this)
            if (data.isEmpty()) { binding.imgWhishlistEmpty.visibility = View.VISIBLE }

            binding.recyclerWishlistMovie.adapter = myAdapter
            binding.recyclerWishlistMovie.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            myAdapter.notifyDataSetChanged()
        }
    }

    override fun onUnLikeClicked(movie: MovieDetailData, position: Int) {
        myAdapter.removeMovie(movie, position)
        data.remove(movie)
        viewModel.deleteMovieFromDatabase(movie)
    }

    override fun onItemClicked(movie: MovieDetailData) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, DetailFragment(movie.movieId))
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        initialRecycler()
    }
}