package com.swallow.cracker.ui.adapters.onboarding

import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.databinding.OnboardingItemBinding
import com.swallow.cracker.ui.model.OnBoardingUI

class OnBoardingViewHolder(private val viewBinding: OnboardingItemBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(items: OnBoardingUI) {
        setImage(items.image)
        setTitle(items.title)
        setDescription(items.description)
    }

    private fun setImage(image: Int) {
        viewBinding.onBoardingImage.setImageResource(image)
    }

    private fun setTitle(title: Int) {
        viewBinding.onBoardingTitle.text = viewBinding.root.context.getString(title)
    }

    private fun setDescription(description: Int) {
        viewBinding.onBoardingDescription.text = viewBinding.root.context.getString(description)
    }
}