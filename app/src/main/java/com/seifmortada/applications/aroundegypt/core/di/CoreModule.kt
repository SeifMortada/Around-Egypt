package com.seifmortada.applications.aroundegypt.core.di

import androidx.room.Room
import com.seifmortada.applications.aroundegypt.core.data.local.ExperienceDatabase
import com.seifmortada.applications.aroundegypt.core.data.repository.ExperienceRepositoryImpl
import com.seifmortada.applications.aroundegypt.core.domain.ExperienceRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    single {
        Room.databaseBuilder(androidApplication(), ExperienceDatabase::class.java, "experience_db")
            .build()
    }
    single {
        get<ExperienceDatabase>().dao
    }

    single {
        networkModule
    }

    singleOf(::ExperienceRepositoryImpl).bind<ExperienceRepository>()
}