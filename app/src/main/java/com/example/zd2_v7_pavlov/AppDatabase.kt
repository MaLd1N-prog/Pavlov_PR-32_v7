package com.example.zd2_v7_pavlov

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Artist::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun artistDao(): ArtistDao
}