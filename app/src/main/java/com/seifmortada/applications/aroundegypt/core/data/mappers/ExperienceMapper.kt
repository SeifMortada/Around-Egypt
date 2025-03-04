package com.seifmortada.applications.aroundegypt.core.data.mappers

import com.seifmortada.applications.aroundegypt.core.data.local.RecentExperiencesEntity
import com.seifmortada.applications.aroundegypt.core.data.local.RecommendedExperienceEntity
import com.seifmortada.applications.aroundegypt.core.data.network.response.ExperienceItemDto
import com.seifmortada.applications.aroundegypt.core.domain.Experience

fun RecommendedExperienceEntity.toExperience(): Experience {
    return Experience(
        id = experienceId,
        title = title,
        description = description,
        imgSrc = imgSrc,
        numberOfViews = numberOfViews,
        numberOfLikes = numberOfLikes,
        recommended = recommended,
        isLiked = isLiked
    )
}
fun RecentExperiencesEntity.toExperience(): Experience {
    return Experience(
        id = experienceId,
        title = title,
        description = description,
        imgSrc = imgSrc,
        numberOfViews = numberOfViews,
        numberOfLikes = numberOfLikes,
        recommended = recommended,
        isLiked = isLiked
    )
}

fun Experience.toRecommendedExperienceEntity(): RecommendedExperienceEntity {
    return RecommendedExperienceEntity(
        experienceId = id,
        title = title,
        description = description,
        imgSrc = imgSrc,
        numberOfViews = numberOfViews,
        numberOfLikes = numberOfLikes,
        recommended = recommended,
        isLiked = isLiked
    )
}
fun Experience.toRecentExperiencesEntity(): RecentExperiencesEntity {
    return RecentExperiencesEntity(
        experienceId = id,
        title = title,
        description = description,
        imgSrc = imgSrc,
        numberOfViews = numberOfViews,
        numberOfLikes = numberOfLikes,
        recommended = recommended,
        isLiked = isLiked
    )
}
fun ExperienceItemDto.toExperience(): Experience {
    return Experience(
        id = id,
        title = title,
        description = description,
        imgSrc = imgSrc,
        numberOfViews = numberOfViews,
        numberOfLikes = numberOfLikes,
        recommended = recommended,
        isLiked = false
    )
}