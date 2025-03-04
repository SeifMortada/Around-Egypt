package com.seifmortada.applications.aroundegypt.core.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LikeExperienceResponse(
    val meta: ResponseCode,
    @SerialName("data") val newLikesCount: Int
)
