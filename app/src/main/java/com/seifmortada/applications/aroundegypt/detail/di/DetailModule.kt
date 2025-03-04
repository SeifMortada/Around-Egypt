package com.seifmortada.applications.aroundegypt.detail.di

import com.seifmortada.applications.aroundegypt.detail.presentation.DetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    viewModel { DetailViewModel(get()) }
}