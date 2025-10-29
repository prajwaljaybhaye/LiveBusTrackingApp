package com.litsbros.livebustrackingsystem.backand.ViewModel.AdminViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litsbros.livebustrackingsystem.backand.ApiInterface.AdminApi

class AdminViewModelFactory(
    private val api: AdminApi,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AdminViewModel(api, context) as T
    }
}
