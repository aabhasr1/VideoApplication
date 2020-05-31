package `in`.aabhas.videoapplication.di

import `in`.aabhas.videoapplication.ui.screen1.Screen1ViewModel
import `in`.aabhas.videoapplication.ui.screen2.Screen2ViewModel
import `in`.aabhas.videoapplication.ui.screen3.Screen3ViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { Screen1ViewModel() }
    viewModel { Screen2ViewModel() }
    viewModel { Screen3ViewModel() }
}