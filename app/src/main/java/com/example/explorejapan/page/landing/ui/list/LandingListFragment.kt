package com.example.explorejapan.page.landing.ui.list

import android.content.Context
import android.os.Bundle
import android.text.Layout.Directions
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast.LENGTH_LONG
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.explorejapan.R
import com.example.explorejapan.databinding.LandingListFragmentBinding
import com.example.explorejapan.datamodel.landing.LandingItem
import com.example.explorejapan.listener.ItemClickListener
import com.example.explorejapan.page.landing.ui.list.adapter.LandingListAdapter
import com.example.explorejapan.page.landing.vm.LandingUiState.Empty
import com.example.explorejapan.page.landing.vm.LandingUiState.ErrorPage
import com.example.explorejapan.page.landing.vm.LandingUiState.ListLoading
import com.example.explorejapan.page.landing.vm.LandingUiState.Loading
import com.example.explorejapan.page.landing.vm.LandingUiState.Success
import com.example.explorejapan.page.landing.vm.LandingUiState.Toast
import com.example.explorejapan.page.landing.vm.LandingViewModel
import com.example.explorejapan.widget.ViewStateWidget.Listener
import kotlinx.coroutines.launch

class LandingListFragment : Fragment() {

    private lateinit var binding: LandingListFragmentBinding
    private lateinit var adapter: LandingListAdapter
    private val viewModel: LandingViewModel by activityViewModels()
    private var loadFromCache: Boolean = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //load from cache only if fragment already attached before
        loadFromCache = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = LandingListFragmentBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initViewStateWidget()
        initSwipeToRefresh()
        viewModel.reloadLanding(loadFromCache = savedInstanceState != null || loadFromCache)
        loadFromCache = true

        with(viewLifecycleOwner) {
            this.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect {
                        when (it) {
                            is Success -> {
                                if (it.addition) {
                                    adapter.addDataSet(it.data)
                                } else {
                                    adapter.setDataSet(it.data)
                                }
                                binding.swipeRefresh.isRefreshing = false
                            }
                            Empty -> {
                                binding.viewStateWidget.showCommonEmpty()
                            }
                            ErrorPage -> {
                                binding.viewStateWidget.showCommonError()
                            }
                            is ListLoading -> {
                               if (it.isLoading){
                                   adapter.showLoading()
                               } else {
                                   adapter.hideLoading()
                               }
                            }
                            is Loading -> {
                                if (it.isLoading) {
                                    binding.loadingStateWidget.show()
                                } else {
                                    binding.loadingStateWidget.hide()
                                    binding.viewStateWidget.hide()
                                }
                            }
                            is Toast -> {
                                android.widget.Toast.makeText(context, it.text, LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.reloadLanding(showLoading = false)
        }
    }

    private fun initRecyclerView() {
        adapter = LandingListAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        adapter.listener = object : ItemClickListener<LandingItem> {
            override fun onItemClickListener(position: Int, item: LandingItem) {
                viewModel.selectedItem = item

                val bundle = bundleOf(
                    "title" to context?.getString(R.string.app_name) + " - " + item.title
                )
                findNavController().navigate(
                    R.id.action_landingListFragment_to_landingDetailFragment,
                    bundle
                )
            }
        }
    }

    private fun initViewStateWidget() {
        binding.viewStateWidget.mListener = object : Listener {
            override fun onRetryClickListener() {
                viewModel.reloadLanding()
            }
        }
    }
}