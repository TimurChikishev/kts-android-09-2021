package com.swallow.cracker.ui.model

import android.os.Parcelable
import com.swallow.cracker.data.model.listing.RedditChildrenPreview
import com.swallow.cracker.utils.convertLongToTime
import kotlinx.parcelize.Parcelize

@Parcelize
class RedditListItemImage(
    override var id: String,
    override var prefixId: String,
    override var author: String,
    override var subreddit: String,
    override var subredditId: String,
    override var title: String,
    override var selfText: String,
    override var score: Int,
    override var likes: Boolean?,
    override var saved: Boolean,
    override var numComments: Int,
    override var created: Long,
    var thumbnail: String,
    override var url: String,
    var preview: RedditChildrenPreview?,
    override var communityIcon: String?
) : RedditItem, Parcelable {

    override val time: String
        get() = created.convertLongToTime()

    override fun equalsContent(other: Any?) = this === other

    override fun thumbnail() = thumbnail

    override fun preview() = preview

    override fun plusScore(value: Int) {
        this.score += value
    }

    override fun payload(other: Any): RedditItem.Payload {
        if (other is RedditListSimpleItem) {
            if (likes != other.likes) {
                return ChangePayload.LikeChanged(other.likes, other.score)
            }

            if (saved != other.saved) {
                return ChangePayload.SavedChanged(other.saved)
            }
        }
        return RedditItem.Payload.None
    }
}