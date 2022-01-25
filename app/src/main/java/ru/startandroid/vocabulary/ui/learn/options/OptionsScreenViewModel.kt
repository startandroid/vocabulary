package ru.startandroid.vocabulary.ui.learn.options

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.startandroid.vocabulary.model.dto.SessionOptions
import ru.startandroid.vocabulary.model.dto.WordData
import ru.startandroid.vocabulary.model.repository.PreferencesRepository
import ru.startandroid.vocabulary.model.usecase.GetAllTagsUseCase
import ru.startandroid.vocabulary.model.usecase.GetWordsAccordingToOptionsUseCase
import javax.inject.Inject

@HiltViewModel
class OptionsScreenViewModel @Inject constructor(
    private val getWordsAccordingToOptionsUseCase: GetWordsAccordingToOptionsUseCase,
    private val getAllTagsUseCase: GetAllTagsUseCase,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _data = MutableLiveData<List<WordData>>(emptyList())
    val data: LiveData<List<WordData>> = _data

    private val _chips = MutableLiveData<List<ChipData>>(emptyList())
    val chips: LiveData<List<ChipData>> = _chips

    val count = mutableStateOf(15)

    init {
        viewModelScope.launch {
            _chips.value = getAllTagsUseCase.invoke().map { ChipData(label = it) }
            preferencesRepository.getLastSessionOptions()?.let {
                applyOptions(it)
            }
        }
    }

    fun onChipClick(label: String) {
        _chips.value = _chips.value?.map {
            if (it.label == label) {
                it.copy(isSelected = it.isSelected.not())
            } else {
                it
            }
        }
    }

    fun onPreviewClick() {
        refresh()
        viewModelScope.launch {
            preferencesRepository.saveSessionOptions(currentOptions())
        }
    }

    fun onRefresh() {
        refresh()
    }

    fun onSelectAll() {
        _chips.value = chips.value?.map {
            it.copy(isSelected = true)
        }
    }

    fun onResetAll() {
        _chips.value = chips.value?.map {
            it.copy(isSelected = false)
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _data.value = getWordsAccordingToOptionsUseCase.invoke(
                currentOptions()
            )
        }
    }

    private fun currentOptions(): SessionOptions {
        return SessionOptions(
            count = count.value,
            tags = chips.value?.filter { it.isSelected }?.map { it.label }?.toSet()
                ?: emptySet()
        )
    }

    private fun applyOptions(options: SessionOptions) {
        count.value = options.count
        _chips.value = chips.value?.map {
            it.copy(isSelected = options.tags.contains(it.label))
        }
    }

}