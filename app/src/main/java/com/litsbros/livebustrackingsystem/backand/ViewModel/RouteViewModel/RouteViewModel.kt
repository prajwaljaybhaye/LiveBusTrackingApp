package com.litsbros.livebustrackingsystem.backand.ViewModel.RouteViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.litsbros.livebustrackingsystem.backand.RetrofitClient
import com.litsbros.livebustrackingsystem.backand.model.RouteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RouteViewModel : ViewModel() {
    private val _routes = mutableStateListOf<RouteModel>()
    val routes: List<RouteModel> get() = _routes

    init {
        fetchAllRoutes()
    }

    fun fetchAllRoutes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getAllRoutes()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _routes.clear()
                        _routes.addAll(it)
                    }
                } else {
                    Log.e("RouteViewModel", "API error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("RouteViewModel", "Network error: ${e.message}", e)
            }
        }
    }

    /** 🌀 Re-fetch routes manually (e.g. from UI refresh) */
    fun reloadRoutes() = fetchAllRoutes()

    fun addRoute(route: RouteModel, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.addRoute(route)
                if (response.isSuccessful && response.body() != null) {
                    _routes.add(0, response.body()!!) // add to list
                    onSuccess()
                } else {
                    onError("Failed: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("RouteViewModel", "Error adding route: ${e.message}", e)
                onError(e.message ?: "Unknown error")
            }
        }
    }
}