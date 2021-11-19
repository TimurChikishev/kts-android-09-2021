package com.swallow.cracker.ui.fragments

import android.os.Bundle
import androidx.navigation.fragment.navArgs

class DetailsPostImageFragment : DetailsPostFragment() {

    private val args by navArgs<DetailsPostImageFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setItem(args.post)
    }
}