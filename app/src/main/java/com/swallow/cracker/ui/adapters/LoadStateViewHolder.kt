package com.swallow.cracker.ui.adapters

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.databinding.LoadStateItemBinding

class LoadStateViewHolder(private val viewBinding: LoadStateItemBinding, retry: () -> Unit): RecyclerView.ViewHolder(viewBinding.root) {

    init {
        viewBinding.buttonRetry.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState){
        viewBinding.apply {
            progressBar.isVisible = loadState is LoadState.Loading
            buttonRetry.isVisible = loadState !is LoadState.Loading
            textViewError.isVisible = loadState !is LoadState.Loading
        }
    }
}
