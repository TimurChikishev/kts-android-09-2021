package com.swallow.cracker.ui.fragments

import android.os.Bundle

class HomeFragment : ListFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setQuery(QUERY_HOME)
    }

    companion object {
        const val QUERY_HOME = ""
    }
}