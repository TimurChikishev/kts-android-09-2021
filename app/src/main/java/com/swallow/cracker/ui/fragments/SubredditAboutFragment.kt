package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentSubredditAboutBinding
import com.swallow.cracker.ui.model.Subreddit
import com.swallow.cracker.ui.viewmodels.SharedSubredditViewModel
import kotlinx.coroutines.flow.collect

class SubredditAboutFragment : Fragment(R.layout.fragment_subreddit_about) {

    private val sharedViewModel: SharedSubredditViewModel by activityViewModels()
    private val viewBinding by viewBinding(FragmentSubredditAboutBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewModel()
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            sharedViewModel.subredditInfo.collect(::setSubredditAboutContent)
        }
    }

    private fun setSubredditAboutContent(subreddit: Subreddit?) {
        subreddit ?: return
        with(viewBinding) {
            descriptionTextView.text = subreddit.description
            publicDescriptionTextView.text = subreddit.publicDescription
        }
    }
}