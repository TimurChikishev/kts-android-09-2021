package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentOnBoardingBinding
import com.swallow.cracker.ui.adapters.OnBoardingAdapter
import com.swallow.cracker.ui.model.OnBoardingUI
import com.swallow.cracker.ui.viewmodels.OnBoardingViewModel
import com.swallow.cracker.utils.autoCleared
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class OnBoardingFragment : Fragment(R.layout.fragment_on_boarding) {

    private val viewBinding by viewBinding(FragmentOnBoardingBinding::bind)
    private val viewModel by viewModels<OnBoardingViewModel>()
    private var adapter: OnBoardingAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.firstLaunchFlow.collect(::checkFirstLaunch)
        }
    }

    private fun checkFirstLaunch(isFirstLaunch: Boolean) {
        if (isFirstLaunch)
            initOnBoarding()
        else
            navigateToAuthFragment()
    }

    private fun initOnBoarding() = with(viewBinding){
        bindingOfClick()

        adapter = OnBoardingAdapter()

        flowActionBar.visibility = View.VISIBLE
        onBoardingVewPager.adapter = adapter
        viewBinding.dotsIndicator.setViewPager2(onBoardingVewPager)
        (onBoardingVewPager.getChildAt(0) as RecyclerView)
            .overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        adapter.setItems(viewModel.getOnBoardingData())
    }

    private fun bindingOfClick() {
        viewBinding.buttonSkip.setOnClickListener { viewModel.initFirstLaunch() }
    }

    private fun navigateToAuthFragment() {
        val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToLogInFragment()
        findNavController().navigate(action)
    }
}