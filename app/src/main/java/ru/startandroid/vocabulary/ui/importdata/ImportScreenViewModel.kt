package ru.startandroid.vocabulary.ui.importdata

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.startandroid.vocabulary.model.dto.WordDataNew
import ru.startandroid.vocabulary.model.usecase.AddWordsUseCase
import ru.startandroid.vocabulary.model.usecase.ImportWordsFromFileUseCase

import javax.inject.Inject

@HiltViewModel
class ImportScreenViewModel @Inject constructor(
    private val importWordsFromFileUseCase: ImportWordsFromFileUseCase,
    private val addWordsUseCase: AddWordsUseCase
): ViewModel() {

    private val _data = MutableLiveData<List<WordDataNew>>(emptyList())
    val data: LiveData<List<WordDataNew>> = _data

    fun onFileChosen(uri: Uri?) {
        uri?.let {
            viewModelScope.launch {
                _data.value = importWordsFromFileUseCase.invoke(it)
            }
        }
    }

    fun onSubmitClick() {
        viewModelScope.launch {
            addWordsUseCase.invoke(data.value!!)
        }
    }

}