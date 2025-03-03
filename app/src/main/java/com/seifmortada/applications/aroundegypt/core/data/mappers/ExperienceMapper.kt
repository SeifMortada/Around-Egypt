package com.seifmortada.applications.aroundegypt.core.data.mappers

import com.seifmortada.applications.aroundegypt.core.data.local.ExperienceEntity
import com.seifmortada.applications.aroundegypt.core.data.network.response.ExperienceItemDto
import com.seifmortada.applications.aroundegypt.core.domain.Experience

fun ExperienceEntity.toExperience(): Experience {
    return Experience(
        id = experienceId,
        title = title,
        description = description,
        imgSrc = imgSrc,
        numberOfViews = numberOfViews,
        numberOfLikes = numberOfLikes
    )
}

fun Experience.toExperienceEntity(): ExperienceEntity {
    return ExperienceEntity(
        experienceId = id,
        title = title,
        description = description,
        imgSrc = imgSrc,
        numberOfViews = numberOfViews,
        numberOfLikes = numberOfLikes
    )
}
fun ExperienceItemDto.toExperience(): Experience {
    return Experience(
        id = id,
        title = title,
        description = description,
        imgSrc = imgSrc,
        numberOfViews = numberOfViews,
        numberOfLikes = numberOfLikes
    )
}