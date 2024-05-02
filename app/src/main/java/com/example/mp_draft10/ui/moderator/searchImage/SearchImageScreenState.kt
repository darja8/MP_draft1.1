package com.example.mp_draft10.ui.moderator.searchImage

import com.example.mp_draft10.data.entities.MappedImageItemModel


data class SearchImageState(

    val isLoading: Boolean = false,
    val error: String? = null,
    val success: List<MappedImageItemModel> = emptyList(),
    val query: String? = null,
    val currentImageNode: MappedImageItemModel? = null
)

sealed class SearchImageEvent {

    object ErrorDismissed : SearchImageEvent()

    class InitiateSearch(val query: String) : SearchImageEvent()

    class QueryChanged(val query: String) :
        SearchImageEvent()

    class OnError(val error: String) :
        SearchImageEvent()

    class UpdateCurrentItem(val item: MappedImageItemModel) :
        SearchImageEvent()

   /* class UpdateCurrentImageNode(val imageItem: com.example.mp_draft10.ui.moderator.ImageItem) :
        ImageSearchEvent()*/

}