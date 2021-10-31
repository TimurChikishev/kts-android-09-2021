package com.swallow.cracker.ui.adapters.listing.viewholders

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.databinding.LoadStateItemBinding

class LoadStateViewHolder(private val viewBinding: LoadStateItemBinding, retry: () -> Unit) :
    RecyclerView.ViewHolder(viewBinding.root) {

    init {
        viewBinding.buttonRetry.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState) = with(viewBinding) {
        progressBar.isVisible = loadState is LoadState.Loading
        buttonRetry.isVisible = loadState is LoadState.Error
        textViewError.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
    }
}
