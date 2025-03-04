package com.seifmortada.applications.aroundegypt.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RecommendedExperienceEntity::class,RecentExperiencesEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ExperienceDatabase : RoomDatabase() {
    abstract val dao: ExperienceDao
}