
package com.foo.pomodoro.viewmodels


import androidx.lifecycle.*
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.data.PomodoroRepository
import com.foo.pomodoro.data.Tag
import com.foo.pomodoro.data.TagRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch


class TagViewModel(
    private val tagRepository: TagRepository
) : ViewModel() {

    val TAG = "TagViewModel"
    val allTags : LiveData<List<Tag>> = tagRepository.allTags.asLiveData()

    fun addTag(title: String) {
        if (title.isNullOrEmpty()) return

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