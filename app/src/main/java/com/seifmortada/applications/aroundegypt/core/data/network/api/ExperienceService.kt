package com.seifmortada.applications.aroundegypt.core.data.network.api

import com.seifmortada.applications.aroundegypt.core.data.network.api.ApiConstant.GET_RECOMMENDED_Experiences_ENDPOINT
import com.seifmortada.applications.aroundegypt.core.data.network.response.ExperienceDtoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExperienceService {

    @GET(GET_RECOMMENDED_Experiences_ENDPOINT)
    suspend fun getRecommendedExperiences(
        @Query("recommended") isRecommended: Boolean = true
    ): Response<ExperienceDtoResponse>

    @GET(GET_RECOMMENDED_Experiences_ENDPOINT)
    suspend fun getRecentExperiences(): Response<ExperienceDtoResponse>

    @GET(GET_RECOMMENDED_Experiences_ENDPOINT)
    suspend fun searchExperiences(
        @Query("title") query: String
    ): Response<ExperienceDtoResponse>

    @GET("$GET_RECOMMENDED_Experiences_ENDPOINT/{id}")
    suspend fun getSingleExperience(@Path("id") id: String): Response<ExperienceDtoResponse>

    @GET("$GET_RECOMMENDED_Experiences_ENDPOINT/{id}/like")
    suspend fun likeExperience(@Path("id") id: String): Response<ExperienceDtoResponse>
}