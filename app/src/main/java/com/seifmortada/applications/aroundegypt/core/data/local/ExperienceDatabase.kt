package com.seifmortada.applications.aroundegypt.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ExperienceEntity::class],
    version = 1
)
abstract class ExperienceDatabase : RoomDatabase() {
    abstract val dao: ExperienceDao
}