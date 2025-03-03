package com.seifmortada.applications.aroundegypt.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Experience_Table")
data class ExperienceEntity(
    @PrimaryKey(autoGenerate = false) val experienceId: String,
    val title: String,
    val description: String,
    val imgSrc: String,
    val numberOfViews: Int,
    val numberOfLikes: Int
)
