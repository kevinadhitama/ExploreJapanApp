package com.example.explorejapan.page.landing.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.explorejapan.databinding.LandingDetailFragmentBinding
import com.example.explorejapan.page.landing.vm.LandingViewModel
import com.example.explorejapan.utils.TextUtil

class LandingDetailFragment : Fragment() {

    private lateinit var binding: LandingDetailFragmentBinding
    private val viewModel: LandingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = LandingDetailFragmentBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val selectedItem = viewModel.selectedItem
        if (selectedItem == null) {
            findNavController().navigateUp()
            return
        }

        selectedItem.let { item ->
            Glide.with(this).clear(binding.landingDetailFragmentImageViewBanner)
            item.imageUrl?.let {
                Glide.with(this)
                    .load(it)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.landingDetailFragmentImageViewBanner)
            }
            binding.landingDetailFragmentTextViewTitle.text = TextUtil.fromHtml(item.title)
            item.description?.let {
                binding.landingDetailFragmentTextViewDescription.text = TextUtil.fromHtml(it)
                binding.landingDetailFragmentTextViewDescription.visibility = View.VISIBLE
            } ?: run {
                binding.landingDetailFragmentTextViewDescription.visibility = View.GONE
            }

        }
    }
}