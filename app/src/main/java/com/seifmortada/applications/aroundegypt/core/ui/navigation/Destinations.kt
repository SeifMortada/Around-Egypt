package com.seifmortada.applications.aroundegypt.core.ui.navigation

import kotlinx.serialization.Serializable

object Destinations {

    @Serializable
    data object HOME

    @Serializable
    data class DETAILS(val experienceId: String)

}