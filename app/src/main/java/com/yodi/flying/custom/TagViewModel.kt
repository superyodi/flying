
package com.yodi.flying.custom


import androidx.lifecycle.*
import com.yodi.flying.model.entity.Tag
import com.yodi.flying.model.repository.TagRepository
import com.yodi.flying.utils.Constants
import kotlinx.coroutines.launch


class TagViewModel(
    private val tagRepository: TagRepository
) : ViewModel() {

    val allTags : LiveData<List<Tag>> = tagRepository.getTags().asLiveData()

    fun addTag(title: String) {
        if (title.isEmpty()) return

        tagRepository.insert(title)

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