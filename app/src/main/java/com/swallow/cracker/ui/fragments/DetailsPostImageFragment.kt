package com.swallow.cracker.ui.fragments

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import timber.log.Timber

class DetailsPostImageFragment : DetailsPostFragment() {

    private val args by navArgs<DetailsPostImageFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag("TAG").d("thumbnail = ${args.post.thumbnail}, preview = ${args.post.preview}")
        setItem(args.post)
    }
}