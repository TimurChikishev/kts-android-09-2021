package com.swallow.cracker.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentOnBoardingBinding
import com.swallow.cracker.ui.adapters.OnBoardingAdapter
import com.swallow.cracker.ui.modal.OnBoardingUI
import com.swallow.cracker.utils.autoCleared

class OnBoardingFragment : Fragment(R.layout.fragment_on_boarding) {

    private val viewBinding by viewBinding(FragmentOnBoardingBinding::bind)
    private var adapter: OnBoardingAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        viewBinding.buttonSkip.setOnClickListener {
            val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToLogInFragment()
            findNavController().navigate(action)
        }
    }

    private fun initAdapter() {
        adapter = OnBoardingAdapter()

        val viewPager = viewBinding.onBoardingVewPager
        viewPager.adapter = adapter
        viewBinding.dotsIndicator.setViewPager2(viewPager)
        (viewPager.getChildAt(0) as RecyclerView)
            .overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        adapter.setItems(
            listOf(
                OnBoardingUI(
                    image = R.drawable.ic_launcher_background,
                    title = "First Title",
                    description = "First description special for on boarding fragment"
                ), OnBoardingUI(
                    image = R.drawable.ic_launcher_background,
                    title = "Second Title",
                    description = "Second description special for on boarding fragment"
                ), OnBoardingUI(
                    image = R.drawable.ic_launcher_background,
                    title = "Third Title",
                    description = "Third description special for on boarding fragment"
                )
            )
        )
    }
}