package ru.startandroid.vocabulary.ui.importdata

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.startandroid.vocabulary.data.ResourceProvider
import ru.startandroid.vocabulary.model.dto.WordDataFile

import javax.inject.Inject

@HiltViewModel
class ImportDataViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider
): ViewModel() {

    private val _data = MutableLiveData<List<WordDataFile>>(emptyList())
    val data: LiveData<List<WordDataFile>> = _data

    fun onFileChosen(uri: Uri?) {
        uri?.let {
            try {
                val s = resourceProvider.openInputStream(it)
                    ?.readBytes()
                    ?.toString(Charsets.UTF_8)
                    ?.split("\n")
                    ?.map {
                        val data = it.split("###")
                        WordDataFile(
                            word = data[0],
                            translate = data[1],
                            tags = data[2].split(",").toSet()
                        )
                    }
                s?.let { _data.value = it }
            } catch (e: Exception) {
                // TODO return as a message to the screen
            }
        }
    }

    fun onSubmitClick() {

    }

}