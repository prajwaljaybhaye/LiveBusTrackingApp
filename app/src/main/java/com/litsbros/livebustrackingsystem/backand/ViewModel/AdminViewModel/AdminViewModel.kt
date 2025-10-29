package com.litsbros.livebustrackingsystem.backand.ViewModel.AdminViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.litsbros.livebustrackingsystem.SessionManager.SessionManager
import com.litsbros.livebustrackingsystem.backand.ApiInterface.AdminApi
import com.litsbros.livebustrackingsystem.backand.model.AdminLogin
import com.litsbros.livebustrackingsystem.backand.model.Admin
import kotlinx.coroutines.launch

class AdminViewModel(
    private val api: AdminApi,
    private val context: Context
) : ViewModel() {

    private val sessionManager = SessionManager(context)

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> get() = _statusMessage

    fun login(adminId: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = api.loginAdmin(AdminLogin(adminId, password))
                if (response.status == "success" && response.user != null) {
                    sessionManager.saveAdminSession(
                        id = response.user.id,
                        adminId = response.user.admin_id,
                        fullName = response.user.full_name,
                        email = response.user.email,
                        phoneNumber = response.user.phone_number,
                        role = response.user.role
                    )
                    _statusMessage.value = "Login successful"
                } else {
                    _statusMessage.value = response.message
                }
            } catch (e: Exception) {
                _statusMessage.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        sessionManager.clearAdminSession()
        _statusMessage.value = "Logged out"
    }

    fun clearStatusMessage() {
        _statusMessage.value = ""
    }

    fun getLoggedInAdmin(): Admin? {
        return sessionManager.getSavedAdmin()
    }
}
