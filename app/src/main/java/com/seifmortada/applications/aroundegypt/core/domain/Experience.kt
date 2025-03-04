package com.seifmortada.applications.aroundegypt.core.domain

import kotlinx.serialization.Serializable

@Serializable
data class Experience(
    val id: String,
    val title: String,
    val description: String,
    val imgSrc: String,
    val numberOfViews: Int,
    val numberOfLikes: Int,
    val recommended: Int,
    val isLiked: Boolean,
    val address:String
)
