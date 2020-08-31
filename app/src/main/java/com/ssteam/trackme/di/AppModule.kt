/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ssteam.trackme.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.ssteam.trackme.data.db.AppDatabase
import com.ssteam.trackme.domain.TrackingSession
import com.ssteam.trackme.domain.TrackingSessionIpm
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun providedTrackingSession(context: Context) : TrackingSession{
        return TrackingSessionIpm(context)
    }
    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        val DATABASE_NAME = "Trackme.DB"
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

}
