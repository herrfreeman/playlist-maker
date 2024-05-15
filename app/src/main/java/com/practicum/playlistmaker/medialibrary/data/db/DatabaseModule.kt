package com.practicum.playlistmaker.medialibrary.data.db

import androidx.room.Room
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            context = get(),
            klass = AppDatabase::class.java,
            name = "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

}