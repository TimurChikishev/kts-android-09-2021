package com.swallow.cracker.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.R
import com.swallow.cracker.ui.modal.OnBoardingUI

class OnBoardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val image: ImageView = itemView.findViewById(R.id.onBoardingImage)
    private val title: TextView = itemView.findViewById(R.id.onBoardingTitle)
    private val description: TextView = itemView.findViewById(R.id.onBoardingDescription)

    fun bind(items: OnBoardingUI) {
        setImage(items.image)
        setTitle(items.title)
        setDescription(items.description)
    }

    private fun setImage(image: Int) {
        this.image.setImageResource(image)
    }

    private fun setTitle(title: String) {
        this.title.text = title
    }

    private fun setDescription(description: String) {
        this.description.text = description
    }
}