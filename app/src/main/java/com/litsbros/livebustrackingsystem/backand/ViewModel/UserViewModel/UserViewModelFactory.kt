package com.litsbros.livebustrackingsystem.backand.ViewModel.UserViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litsbros.livebustrackingsystem.backand.ApiInterface.AdminApi

class UserViewModelFactory(
    private val api: AdminApi,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(api, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
