package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.swallow.cracker.R
import com.swallow.cracker.data.model.RedditChildrenPreview
import com.swallow.cracker.databinding.FragmentDetailsBinding
import com.swallow.cracker.ui.model.RedditListItemImage
import com.swallow.cracker.ui.viewmodels.NetworkStatusViewModel
import com.swallow.cracker.ui.viewmodels.PostDetailViewModel
import com.swallow.cracker.utils.*
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class DetailsPostImageFragment : DetailsPostFragment() {

    private val args by navArgs<DetailsPostImageFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag("TAG").d("thumbnail = ${args.post.thumbnail}, preview = ${args.post.preview}")
        setItem(args.post)
    }
}