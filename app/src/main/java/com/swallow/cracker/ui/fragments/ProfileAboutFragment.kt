package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentProfiletAboutBinding
import com.swallow.cracker.ui.model.RedditProfile
import com.swallow.cracker.ui.viewmodels.SharedProfileViewModel
import kotlinx.coroutines.flow.collect

class ProfileAboutFragment : Fragment(R.layout.fragment_profilet_about) {

    private val viewBinding by viewBinding(FragmentProfiletAboutBinding::bind)
    private val sharedViewModel: SharedProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewModel()
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            sharedViewModel.redditProfile.collect(::setProfileAboutContent)
        }
    }

    private fun setProfileAboutContent(profile: RedditProfile?) {
        profile ?: return

        with(viewBinding) {
            postKarmaTextView.text = profile.linkKarma.toString()
            commentsKarmaTextView.text = profile.commentKarma.toString()
            awardeeKarmaTextView.text = profile.awardeeKarma.toString()
            awarderKarmaTextView.text = profile.awarderKarma.toString()
        }
    }
}