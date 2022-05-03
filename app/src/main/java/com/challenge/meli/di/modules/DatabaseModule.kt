package com.challenge.meli.di.modules

import android.app.Application
import androidx.room.Room
import com.challenge.meli.data.database.RecentSearchDao
import com.challenge.meli.data.database.RecentSearchDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): RecentSearchDataBase {
        return Room.databaseBuilder(
            application, RecentSearchDataBase::class.java, "recent-search-db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRecentSearchDao(recentSearchDataBase: RecentSearchDataBase): RecentSearchDao {
        return recentSearchDataBase.recentSearchDao()
    }
}