package ru.startandroid.vocabulary.ui.learn.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.startandroid.vocabulary.model.dto.SessionOptions
import ru.startandroid.vocabulary.model.dto.WordData
import ru.startandroid.vocabulary.model.usecase.GetAllTagsUseCase
import ru.startandroid.vocabulary.model.usecase.GetWordsAccordingToOptionsUseCase
import javax.inject.Inject

@HiltViewModel
class OptionsScreenViewModel @Inject constructor(
    private val getWordsAccordingToOptionsUseCase: GetWordsAccordingToOptionsUseCase,
    private val getAllTagsUseCase: GetAllTagsUseCase
) : ViewModel() {

    private val _data = MutableLiveData<List<WordData>>(emptyList())
    val data: LiveData<List<WordData>> = _data

    private val _chips = MutableLiveData<List<ChipData>>(emptyList())
    val chips: LiveData<List<ChipData>> = _chips

    init {
        viewModelScope.launch {
            _chips.value = getAllTagsUseCase.invoke().map { ChipData(label = it) }
        }
    }

    var count = 0

    fun onChipClick(label: String) {
        _chips.value = _chips.value?.map {
            if (it.label == label) {
                it.copy(isSelected = it.isSelected.not())
            } else {
                it
            }
        }
    }

    fun onPreviewClick(count: Int) {
        this.count = count
        refresh()
    }

    fun onRefresh() {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            _data.value = getWordsAccordingToOptionsUseCase.invoke(
                SessionOptions(
                    count = count,
                    tags = chips.value?.filter { it.isSelected }?.map { it.label }?.toSet()
                        ?: emptySet()
                )
            )
        }
    }

}