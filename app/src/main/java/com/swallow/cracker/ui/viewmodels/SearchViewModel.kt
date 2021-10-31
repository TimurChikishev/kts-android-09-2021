package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.data.repository.RedditRepository
import com.swallow.cracker.ui.model.Subreddit
import com.swallow.cracker.utils.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchViewModel : ViewModel() {
    private val repository = RedditRepository()

    private val subredditMutableStateFlow = MutableStateFlow<List<Subreddit>?>(null)

    val subredditsFlow: MutableStateFlow<List<Subreddit>?>
        get() = subredditMutableStateFlow

    @OptIn(FlowPreview::class)
    fun search(query: String) {
        viewModelScope.launch {
            repository.searchSubreddit(query)
                .debounce(500)
                .catch {
                    Timber.tag("ERROR").d("Error $it.")
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    Timber.tag("TAG").d("Success $it.")
                    subredditMutableStateFlow.set(it)
                }

        }
    }
}