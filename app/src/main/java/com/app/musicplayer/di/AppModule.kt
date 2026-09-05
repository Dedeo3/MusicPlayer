package com.app.musicplayer.di

import com.app.musicplayer.data.remote.repository.IMusicRepository
import com.app.musicplayer.data.remote.repository.MusicRepositoryImp
import com.app.musicplayer.ui.view_model.MusicViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<IMusicRepository> { MusicRepositoryImp(get()) }

    viewModel { MusicViewModel(get()) }
}