package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentSearchBinding
import com.swallow.cracker.ui.adapters.search.delegates.ComplexSearchDelegatesListAdapter
import com.swallow.cracker.ui.viewmodels.SearchViewModel
import com.swallow.cracker.utils.autoCleared
import kotlinx.coroutines.flow.collect

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val searchViewModel: SearchViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentSearchBinding::bind)
    private var searchAdapter: ComplexSearchDelegatesListAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        bindingOfClick()
        bindSearchView()
        bindViewModel()
    }

    private fun initAdapter() {
        searchAdapter = ComplexSearchDelegatesListAdapter()
        with(viewBinding.searchRecyclerView) {
            val orientation = RecyclerView.VERTICAL

            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context, orientation, false)

            addItemDecoration(DividerItemDecoration(context, orientation))
            setHasFixedSize(true)
        }
    }

    private fun bindViewModel() = with(viewLifecycleOwner.lifecycleScope) {
        launchWhenCreated {
            searchViewModel.subredditsFlow.collect {
                it?.let { searchAdapter.updateItems(it) }
            }
        }
    }

    private fun bindSearchView() {
        viewBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchViewModel.search(query)
                    return true
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    searchViewModel.search(newText)
                    return true
                }
                return false
            }
        })
    }

    private fun bindingOfClick() = with(viewBinding) {
        backMaterialButton.setOnClickListener { findNavController().popBackStack() }
    }
}