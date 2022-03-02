package com.example.explorejapan.page.landing.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.explorejapan.R
import com.example.explorejapan.databinding.LandingListFragmentBinding
import com.example.explorejapan.datamodel.landing.LandingItem
import com.example.explorejapan.listener.ItemClickListener
import com.example.explorejapan.page.landing.ui.list.adapter.LandingListAdapter
import com.example.explorejapan.page.landing.vm.LandingUiState
import com.example.explorejapan.page.landing.vm.LandingUiState.Empty
import com.example.explorejapan.page.landing.vm.LandingUiState.ErrorPage
import com.example.explorejapan.page.landing.vm.LandingUiState.ListLoading
import com.example.explorejapan.page.landing.vm.LandingUiState.Loading
import com.example.explorejapan.page.landing.vm.LandingUiState.Success
import com.example.explorejapan.page.landing.vm.LandingViewModel
import kotlinx.coroutines.launch

class LandingListFragment : Fragment() {

    private lateinit var binding: LandingListFragmentBinding
    private val viewModel: LandingViewModel by activityViewModels()
    private val adapter: LandingListAdapter = LandingListAdapter()

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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is Success -> {
                            if (it.addition) {
                                adapter.addDataSet(it.data)
                            } else {
                                adapter.setDataSet(it.data)
                            }
                        }
                        Empty -> {

                        }
                        is Error -> {

                        }
                        is LandingUiState.Error -> {

                        }
                        ErrorPage -> {

                        }
                        is ListLoading -> {

                        }
                        is Loading -> {

                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
        adapter.listener = object : ItemClickListener<LandingItem> {
            override fun onItemClickListener(position: Int, item: LandingItem) {
                findNavController().navigate(R.id.action_landingListFragment_to_landingDetailFragment)
            }
        }
    }
}