package com.swallow.cracker.ui.model

import android.os.Parcelable
import com.swallow.cracker.data.model.RedditChildrenPreview
import com.swallow.cracker.utils.convertLongToTime
import kotlinx.parcelize.Parcelize

@Parcelize
class RedditListItemImage(
    var id: String,
    var t3_id: String,
    var author: String,
    var subreddit: String,
    val subredditId: String,
    var title: String,
    var selftext: String,
    var score: Int,
    var likes: Boolean?,
    var saved: Boolean,
    var numComments: Int,
    var created: Long,
    var thumbnail: String,
    var url: String,
    val preview: RedditChildrenPreview?,
    val communityIcon: String?
) : RedditItem, Parcelable {

    val time: String
        get() = created.convertLongToTime()

    override fun id() = t3_id

    override fun equalsContent(other: Any?) = this === other

    override fun likes() = likes

    override fun setItemLikes(likes: Boolean?) {
        this.likes = likes
    }

    override fun setItemSaved(saved: Boolean) {
        this.saved = saved
    }

    override fun plusScore(value: Int) {
        this.score += value
    }

    override fun score() = score

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