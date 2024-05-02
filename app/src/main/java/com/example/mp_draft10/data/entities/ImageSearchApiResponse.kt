package com.example.mp_draft10.data.entities

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parceler

data class PixabayResponse(

	@Json(name="hits")
	val hits: List<ImageItem> = listOf(),

	@Json(name="total")
	val total: Int? = null,

	@Json(name="totalHits")
	val totalHits: Int? = null
)

@kotlinx.parcelize.Parcelize
data class ImageItem(

	@Json(name="webformatHeight")
	val webformatHeight: Int? = null,

	@Json(name="imageWidth")
	val imageWidth: Int? = null,

	@Json(name="previewHeight")
	val previewHeight: Int? = null,

	@Json(name="webformatURL")
	val webformatURL: String? = null,

	@Json(name="userImageURL")
	val userImageURL: String? = null,

	@Json(name="previewURL")
	val previewURL: String? = null,

	@Json(name="comments")
	val comments: Int? = null,

	@Json(name="type")
	val type: String? = null,

	@Json(name="imageHeight")
	val imageHeight: Int? = null,

	@Json(name="tags")
	val tags: String? = null,

	@Json(name="previewWidth")
	val previewWidth: Int? = null,

	@Json(name="downloads")
	val downloads: Int? = null,

	@Json(name="collections")
	val collections: Int? = null,

	@Json(name="user_id")
	val userId: Int? = null,

	@Json(name="largeImageURL")
	val largeImageURL: String? = null,

	@Json(name="pageURL")
	val pageURL: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="imageSize")
	val imageSize: Int? = null,

	@Json(name="webformatWidth")
	val webformatWidth: Int? = null,

	@Json(name="user")
	val user: String? = null,

	@Json(name="views")
	val views: Int? = null,

	@Json(name="likes")
	val likes: Int? = null
):Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readString(),
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readString(),
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readString(),
		parcel.readString(),
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readString(),
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readValue(Int::class.java.classLoader) as? Int
	) {
	}

	companion object : Parceler<ImageItem> {

		override fun ImageItem.write(parcel: Parcel, flags: Int) {
			parcel.writeValue(webformatHeight)
			parcel.writeValue(imageWidth)
			parcel.writeValue(previewHeight)
			parcel.writeString(webformatURL)
			parcel.writeString(userImageURL)
			parcel.writeString(previewURL)
			parcel.writeValue(comments)
			parcel.writeString(type)
			parcel.writeValue(imageHeight)
			parcel.writeString(tags)
			parcel.writeValue(previewWidth)
			parcel.writeValue(downloads)
			parcel.writeValue(collections)
			parcel.writeValue(userId)
			parcel.writeString(largeImageURL)
			parcel.writeString(pageURL)
			parcel.writeValue(id)
			parcel.writeValue(imageSize)
			parcel.writeValue(webformatWidth)
			parcel.writeString(user)
			parcel.writeValue(views)
			parcel.writeValue(likes)
		}

		override fun create(parcel: Parcel): ImageItem {
			return ImageItem(parcel)
		}
	}
}

/*data class ImageModel(
	val imageId: Long = -1,
	val userName: String,
	val url: String,
	val likes: String,
	val downloads: String,
	val comments: String,
	val tags: List<String>,
	val largeImageURL: String?,
	val previewURL:String?,
	val userImageURL: String?
)



fun com.example.mp_draft10.ui.moderator.ImageItem.toImageModel() = ImageModel(
	imageId = id?.toLong() ?: -1,
	userName = user ?: "",
	url = previewURL ?: "",
	likes = likes.toString(),
	downloads = downloads.toString(),
	comments = comments.toString(),
	tags = tags?.split(", ") ?: emptyList(),
	largeImageURL = largeImageURL,
	previewURL = previewURL,
	userImageURL= userImageURL
)*/
