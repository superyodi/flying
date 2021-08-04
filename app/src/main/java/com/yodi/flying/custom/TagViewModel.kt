
package com.yodi.flying.custom


import androidx.lifecycle.*
import com.yodi.flying.model.entity.Tag
import com.yodi.flying.model.repository.TagRepository
import com.yodi.flying.utils.Constants
import kotlinx.coroutines.launch


class TagViewModel(
    private val tagRepository: TagRepository
) : ViewModel() {

    val TAG = "TagViewModel"
    val allTags : LiveData<List<Tag>> = tagRepository.getTags(Constants.USER_ID).asLiveData()

    fun addTag(title: String) {
        if (title.isEmpty()) return

        viewModelScope.launch {
            tagRepository.insert(Tag(title))
        }
    }
}


class TagViewModelFactory(val repository: TagRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TagViewModel::class.java)) {
            TagViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}