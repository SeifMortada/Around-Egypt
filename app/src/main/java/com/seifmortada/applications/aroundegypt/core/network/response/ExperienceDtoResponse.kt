package com.seifmortada.applications.aroundegypt.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExperienceDtoResponse(
    val data: List<ExperienceItemDto>,
    @SerialName("meta") val response: ResponseCode
)

@Serializable
data class ExperienceItemDto(
    val id: String,
    val title: String,
    val description: String,
    @SerialName("cover_photo") val imgSrc: String,
    @SerialName("views_no") val numberOfViews: Int,
    @SerialName("Likes_no") val numberOfLikes: Int,
)

@Serializable
data class ResponseCode(
    val code: Int,
    val errors: List<ResponseError>
)

@Serializable
data class ResponseError(
    val type: String,
    val message: String,
    val exception: String
)