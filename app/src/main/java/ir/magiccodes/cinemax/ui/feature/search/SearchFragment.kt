package ir.magiccodes.cinemax.ui.feature.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.magiccodes.cinemax.R
import ir.magiccodes.cinemax.adapter.SearchResultAdapter
import ir.magiccodes.cinemax.databinding.FragmentSearchBinding
import ir.magiccodes.cinemax.model.data.MoviePreviewData
import ir.magiccodes.cinemax.ui.feature.detail.DetailFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment(),SearchResultAdapter.SearchResultEvent {
    lateinit var binding: FragmentSearchBinding
    lateinit var adapter : SearchResultAdapter
    private val viewModel: SearchFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        search()

    }

    private fun initUi() {

        binding.btnCancelSearch.visibility = View.GONE
        binding.edtSearch.addTextChangedListener {
            if (binding.edtSearch.text.isEmpty()) {
                binding.btnCancelSearch.visibility = View.GONE
            } else {
                binding.btnCancelSearch.visibility = View.VISIBLE
            }
        }

        binding.btnCancelSearch.setOnClickListener {
            binding.edtSearch.setText("")
            adapter.removeList()
        }
    }

    private fun search() {
        binding.edtSearch.addTextChangedListener {

            if (it!!.isNotEmpty()){
                binding.imgBeforeSearch.visibility = View.GONE

                viewModel.searchMovie(it.toString())
                viewModel.searchResult.observe(requireActivity()){ data->
                     adapter = SearchResultAdapter(data as ArrayList<MoviePreviewData>,requireContext(),this)

                    binding.recyclerSearchResult.adapter = adapter
                    adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                    binding.recyclerSearchResult.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

                }

            } else {
                binding.imgBeforeSearch.visibility = View.VISIBLE
                adapter.removeList()
            }
        }

    }

    override fun onItemClicked(movieId: Int) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, DetailFragment(movieId))
        transaction.addToBackStack(null)
        transaction.commit()
    }

}