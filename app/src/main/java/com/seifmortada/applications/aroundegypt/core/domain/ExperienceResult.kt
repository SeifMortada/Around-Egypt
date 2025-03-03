package com.seifmortada.applications.aroundegypt.core.domain

sealed class ExperienceResult<T>(val data: T? = null, val errorMessage: String?=null) {
    class Success<T>(data: T?) : ExperienceResult<T>(data=data)
    class Error<T>(errorMessage: String?): ExperienceResult<T>(errorMessage = errorMessage)
    class Loading<T> : ExperienceResult<T>()
}