package com.example.mp_draft10.data.entities


fun ImageItem.toImageModel() = MappedImageItemModel(
    imageId = id?.toLong() ?: -1,
    user = user ?: "",
    url = previewURL ?: "",
    views = views.toString(),
    likes = likes.toString(),
    downloads = downloads.toString(),
    comments = comments.toString(),
    tags = tags?.split(", ") ?: emptyList(),
    largeImageURL = largeImageURL,
    previewURL = previewURL,
    userImageURL= userImageURL
)