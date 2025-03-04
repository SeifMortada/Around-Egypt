package com.seifmortada.applications.aroundegypt.core.data.network.api

import com.seifmortada.applications.aroundegypt.core.data.network.api.ApiConstant.EXPERIENCE_API_ENDPOINT
import com.seifmortada.applications.aroundegypt.core.data.network.response.ExperienceDtoResponse
import com.seifmortada.applications.aroundegypt.core.data.network.response.LikeExperienceResponse
import com.seifmortada.applications.aroundegypt.core.data.network.response.SingleItemSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ExperienceService {

    @GET(EXPERIENCE_API_ENDPOINT)
    suspend fun getRecommendedExperiences(
        @Query("recommended") isRecommended: Boolean = true
    ): Response<ExperienceDtoResponse>

    @GET(EXPERIENCE_API_ENDPOINT)
    suspend fun getRecentExperiences(): Response<ExperienceDtoResponse>

    @GET(EXPERIENCE_API_ENDPOINT)
    suspend fun searchExperiences(
        @Query("title") query: String
    ): Response<ExperienceDtoResponse>

    @GET("$EXPERIENCE_API_ENDPOINT/{id}")
    suspend fun getSingleExperience(@Path("id") id: String): Response<SingleItemSearchResponse>

    @POST("$EXPERIENCE_API_ENDPOINT/{id}/like")
    suspend fun likeExperience(@Path("id") id: String): Response<LikeExperienceResponse>
}