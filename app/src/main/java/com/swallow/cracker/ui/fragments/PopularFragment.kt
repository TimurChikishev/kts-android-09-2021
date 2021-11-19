package com.swallow.cracker.ui.fragments

import android.os.Bundle

class PopularFragment : ListFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setQuery(QUERY_POPULAR)
    }

    companion object {
        const val QUERY_POPULAR = "r/popular"
    }
}