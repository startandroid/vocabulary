package ru.startandroid.vocabulary.data.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.startandroid.vocabulary.data.ResourceProvider
import ru.startandroid.vocabulary.data.db.AppDatabase
import ru.startandroid.vocabulary.data.db.WordsDao
import ru.startandroid.vocabulary.data.mapper.WordMapperDbToUi
import ru.startandroid.vocabulary.data.mapper.WordMapperNewToDb
import ru.startandroid.vocabulary.data.repository.FileRepositoryImpl
import ru.startandroid.vocabulary.data.repository.PreferencesRepositoryImpl
import ru.startandroid.vocabulary.data.repository.WordRepositoryImpl
import ru.startandroid.vocabulary.model.repository.FileRepository
import ru.startandroid.vocabulary.model.repository.PreferencesRepository
import ru.startandroid.vocabulary.model.repository.WordRepository
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.random.Random

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideFileRepository(resourceProvider: ResourceProvider, randomProvider: Provider<Random>): FileRepository {
        return FileRepositoryImpl(resourceProvider, randomProvider)
    }

    @Singleton
    @Provides
    fun provideWordRepository(wordsDao: WordsDao, wordMapperNewToDb: WordMapperNewToDb, wordMapperDbToUi: WordMapperDbToUi): WordRepository {
        return WordRepositoryImpl(wordsDao, wordMapperNewToDb, wordMapperDbToUi)
    }

    @Singleton
    @Provides
    fun providePreferencesRepository(dataStore: DataStore<Preferences>): PreferencesRepository {
        return PreferencesRepositoryImpl(dataStore)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "database").build()
    }

    @Provides
    fun provideWordsDao(appDatabase: AppDatabase): WordsDao {
        return appDatabase.wordDao()
    }

    @Provides
    fun provideRandom(): Random {
        return Random(System.currentTimeMillis() % Integer.MAX_VALUE)
    }

    @Singleton
    @Provides
    fun provideDataStore(app: Application): DataStore<Preferences> {
        return app.dataStore
    }

    private val Context.dataStore by preferencesDataStore(
        name = "preferences"
    )

}