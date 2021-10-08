package com.swallow.cracker.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class VoteTransition(val likes: Boolean, val position: Int?) : Parcelable