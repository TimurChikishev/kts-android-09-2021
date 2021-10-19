package com.swallow.cracker.data.database

import androidx.room.TypeConverter
import com.swallow.cracker.data.model.RedditChildrenPreview
import com.swallow.cracker.data.model.RedditChildrenPreviewImage
import com.swallow.cracker.data.model.RedditChildrenPreviewImageSource
import com.swallow.cracker.utils.fixImgUrl

class TypeConverter {
    @TypeConverter
    fun previewToUrl(preview: RedditChildrenPreview?): String? {
        return preview?.let { preview.images[0].source.urlNew }
    }

    @TypeConverter
    fun urlToPreview(url: String?): RedditChildrenPreview? {
        url ?: return null

        val source = RedditChildrenPreviewImageSource(
            url = url.fixImgUrl(),
            width = 500,
            height = 500,
            urlNew = url.fixImgUrl()
        )

        val images = listOf(RedditChildrenPreviewImage(source))

        return RedditChildrenPreview(images = images, enabled = true)
    }
}