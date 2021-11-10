package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.swallow.cracker.ui.model.Subreddit
import com.swallow.cracker.utils.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedSubredditViewModel : ViewModel() {
    private val subredditMutableStateFlow = MutableStateFlow<Subreddit?>(null)

    val subredditInfo: StateFlow<Subreddit?>
        get() = subredditMutableStateFlow

    private val queryMutableStateFlow = MutableStateFlow<String?>(null)

    val queryStateFlow: StateFlow<String?>
        get() = queryMutableStateFlow

    fun setSubredditInfo(subreddit: Subreddit) {
        subredditMutableStateFlow.set(subreddit)
    }

    fun setQuery(query: String) {
        queryMutableStateFlow.set(query)
    }
}