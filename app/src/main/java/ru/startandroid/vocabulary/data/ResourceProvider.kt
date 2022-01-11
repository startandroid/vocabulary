package ru.startandroid.vocabulary.data

import android.app.Application
import android.net.Uri
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProvider @Inject constructor(
    private val app: Application
) {

    fun openInputStream(uri: Uri): InputStream? =
        app.contentResolver.openInputStream(uri)

}