package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.swallow.cracker.R

class SearchResultFragment : Fragment(R.layout.fragment_search_result) {
    private val args: SearchResultFragmentArgs by navArgs()
    private val query: String by lazy { args.query }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}