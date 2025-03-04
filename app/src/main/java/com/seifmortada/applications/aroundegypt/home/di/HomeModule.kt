package com.seifmortada.applications.aroundegypt.home.di

import com.seifmortada.applications.aroundegypt.home.domain.SearchExperienceUseCase
import com.seifmortada.applications.aroundegypt.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    single {
        SearchExperienceUseCase(repository = get())
    }
    viewModel { HomeViewModel(experienceRepository = get(), searchExperienceUseCase = get()) }
}