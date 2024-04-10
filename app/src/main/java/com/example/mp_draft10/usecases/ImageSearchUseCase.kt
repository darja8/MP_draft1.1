package com.example.mp_draft10.usecases

import com.example.mp_draft10.data.entities.MappedImageItemModel
import com.example.mp_draft10.repository.ImageSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/*Image Search use case*/
class ImageSearchUseCase @Inject constructor(
    private val repository: ImageSearchRepository
) {

    fun execute(query: String): Flow<List<MappedImageItemModel>> = flow {

        emit(repository.fetchSearchData(query)/*.map {
            it.toImageModel()
        }*/)
    }.flowOn(Dispatchers.IO)

}