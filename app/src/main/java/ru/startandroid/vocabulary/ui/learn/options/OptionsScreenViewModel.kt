package ru.startandroid.vocabulary.ui.learn.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.startandroid.vocabulary.model.dto.WordData
import ru.startandroid.vocabulary.model.usecase.GetWordsAccordingToOptionsUseCase
import javax.inject.Inject

@HiltViewModel
class OptionsScreenViewModel @Inject constructor(
    private val getWordsAccordingToOptionsUseCase: GetWordsAccordingToOptionsUseCase
): ViewModel() {

    private val _data = MutableLiveData<List<WordData>>(emptyList())
    val data: LiveData<List<WordData>> = _data
    var count = 0

    fun onPreviewClick(count: Int) {
        this.count = count
        refresh()
    }

    fun onRefresh() {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            _data.value = getWordsAccordingToOptionsUseCase.invoke(count)
        }
    }

}