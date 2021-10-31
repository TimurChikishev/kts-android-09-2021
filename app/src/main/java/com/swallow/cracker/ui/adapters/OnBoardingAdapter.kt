package com.swallow.cracker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.databinding.OnboardingItemBinding
import com.swallow.cracker.ui.adapters.listing.viewholders.OnBoardingViewHolder
import com.swallow.cracker.ui.model.OnBoardingUI

class OnBoardingAdapter : RecyclerView.Adapter<OnBoardingViewHolder>() {

    private var items: List<OnBoardingUI> = listOf()

    fun setItems(items: List<OnBoardingUI>) {
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        return OnBoardingViewHolder(
            OnboardingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}