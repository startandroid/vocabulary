package ru.startandroid.vocabulary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WordDataDb::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun wordDao(): WordsDao
}