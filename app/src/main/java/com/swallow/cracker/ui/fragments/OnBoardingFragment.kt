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
import com.swallow.cracker.UserPreferences
import com.swallow.cracker.databinding.FragmentOnBoardingBinding
import com.swallow.cracker.ui.adapters.OnBoardingAdapter
import com.swallow.cracker.ui.viewmodels.OnBoardingViewModel
import com.swallow.cracker.utils.autoCleared
import com.swallow.cracker.utils.bottomNavigationGone
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take

class OnBoardingFragment : Fragment(R.layout.fragment_on_boarding) {

    private val viewBinding by viewBinding(FragmentOnBoardingBinding::bind)
    private val viewModel by viewModels<OnBoardingViewModel>()
    private var adapter: OnBoardingAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationGone()
        bindingViewModel()
    }

    private fun bindingViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.userPreferencesFlow.take(1).collect(::navigateToStartFragment)
        }
    }

    private fun navigateToStartFragment(pref: UserPreferences) {
        when {
            pref.authToken.isNotEmpty() -> navigateToMainFragment()
            !pref.onBoardingShown -> initOnBoardingFragment()
            pref.onBoardingShown -> navigateToAuthFragment()
            else -> initOnBoardingFragment()
        }
    }

    private fun updateOnBoardingShown() {
        viewModel.updateOnboardShown(true)
    }

    private fun initOnBoardingFragment() {
        bindingOfClick()
        initOnBoarding()
    }

    private fun initOnBoarding() = with(viewBinding) {
        adapter = OnBoardingAdapter()

        buttonSkip.visibility = View.VISIBLE
        onBoardingVewPager.adapter = adapter
        viewBinding.dotsIndicator.setViewPager2(onBoardingVewPager)
        (onBoardingVewPager.getChildAt(0) as RecyclerView)
            .overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        adapter.setItems(viewModel.getOnBoardingData())
    }

    private fun bindingOfClick() {
        viewBinding.buttonSkip.setOnClickListener {
            updateOnBoardingShown()
            navigateToAuthFragment()
        }
    }

    private fun navigateToAuthFragment() {
        val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToAuthFragment()
        findNavController().navigate(action)
    }

    private fun navigateToMainFragment() {
        val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToMainFragment()
        findNavController().navigate(action)
    }
}