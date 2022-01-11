package ru.startandroid.vocabulary.data.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.startandroid.vocabulary.data.ResourceProvider
import ru.startandroid.vocabulary.data.db.AppDatabase
import ru.startandroid.vocabulary.data.db.WordsDao
import ru.startandroid.vocabulary.data.mapper.WordMapperNewToDb
import ru.startandroid.vocabulary.data.repository.FileRepositoryImpl
import ru.startandroid.vocabulary.data.repository.WordRepositoryImpl
import ru.startandroid.vocabulary.model.repository.FileRepository
import ru.startandroid.vocabulary.model.repository.WordRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideFileRepository(resourceProvider: ResourceProvider): FileRepository {
        return FileRepositoryImpl(resourceProvider)
    }

    @Singleton
    @Provides
    fun provideWordRepository(wordsDao: WordsDao, wordMapperNewToDb: WordMapperNewToDb): WordRepository {
        return WordRepositoryImpl(wordsDao, wordMapperNewToDb)
    }

    @Singleton
    @Provides
    fun providesAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "database").build()
    }

    @Provides
    fun providesWordsDao(appDatabase: AppDatabase): WordsDao {
        return appDatabase.wordDao()
    }
}