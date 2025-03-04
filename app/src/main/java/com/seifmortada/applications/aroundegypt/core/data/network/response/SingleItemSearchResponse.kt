package com.seifmortada.applications.aroundegypt.core.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class SingleItemSearchResponse(
    val data: ExperienceItemDto,
    val meta: ResponseCode
)
