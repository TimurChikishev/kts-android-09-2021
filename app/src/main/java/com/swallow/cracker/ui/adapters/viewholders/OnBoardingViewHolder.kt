package com.swallow.cracker.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.databinding.OnboardingItemBinding
import com.swallow.cracker.ui.modal.OnBoardingUI

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

    private fun setTitle(title: String) {
        viewBinding.onBoardingTitle.text = title
    }

    private fun setDescription(description: String) {
        viewBinding.onBoardingDescription.text = description
    }
}