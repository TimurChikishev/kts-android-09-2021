package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentHomeBinding
import com.swallow.cracker.ui.adapters.LoadStateAdapter
import com.swallow.cracker.ui.adapters.listing.delegates.ComplexDelegateAdapterClick
import com.swallow.cracker.ui.adapters.listing.delegates.ComplexDelegatesRedditListAdapter
import com.swallow.cracker.ui.adapters.listing.delegates.items.RedditListItemImageDelegateAdapter
import com.swallow.cracker.ui.adapters.listing.delegates.items.RedditListSimpleItemDelegateAdapter
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.ui.model.RedditListItemImage
import com.swallow.cracker.ui.model.RedditListSimpleItem
import com.swallow.cracker.ui.viewmodels.RedditListViewModel
import com.swallow.cracker.utils.asMergedLoadStates
import com.swallow.cracker.utils.getDataFormCacheSnackBar
import com.swallow.cracker.utils.sharedUrl
import com.swallow.cracker.utils.showMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class HomeFragment : ListFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setQuery(QUERY_HOME)
    }

    companion object {
        const val QUERY_HOME = ""
    }
}