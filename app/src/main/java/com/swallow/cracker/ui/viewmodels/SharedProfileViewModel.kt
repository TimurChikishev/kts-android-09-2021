package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.swallow.cracker.ui.model.RedditProfile
import com.swallow.cracker.utils.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedProfileViewModel : ViewModel() {
    private val profileMutableStateFlow = MutableStateFlow<RedditProfile?>(null)

    val redditProfile: StateFlow<RedditProfile?>
        get() = profileMutableStateFlow

    fun setProfileInfo(profile: RedditProfile) {
        profileMutableStateFlow.set(profile)
    }
}