package com.litsbros.livebustrackingsystem.backand.ViewModel.UserViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.litsbros.livebustrackingsystem.SessionManager.SessionManager
import com.litsbros.livebustrackingsystem.backand.ApiInterface.AdminApi
import com.litsbros.livebustrackingsystem.backand.model.LoginRequest
import com.litsbros.livebustrackingsystem.backand.model.User
import kotlinx.coroutines.launch

class UserViewModel(
    private val api: AdminApi,
    private val context: Context // Needed for SessionManager
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading


    private val sessionManager = SessionManager(context)

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val _selectedUser = MutableLiveData<User?>()
    val selectedUser: LiveData<User?> get() = _selectedUser

    private val _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> get() = _statusMessage

    fun createUser(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = api.createUser(user)
                if (response.status == "success" && response.user_id != null) {
                    sessionManager.saveUserSession(
                        userId = response.user_id,
                        fullName = user.full_name,
                        email = user.email,
                        phoneNumber = user.phone_number
                    )
                    _statusMessage.value = "User created with ID: ${response.user_id}"
                } else {
                    _statusMessage.value = "Create failed: ${response.status ?: "Unknown error"}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "Error creating user: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearStatusMessage() {
        _statusMessage.value = ""
    }

    fun setStatusMessage(message: String) {
        _statusMessage.value = message
    }

    fun fetchLoggedInUser() {
        val userId = sessionManager.getSavedUserId()
        if (userId != null) {
            fetchUserById(userId)
        } else {
            _statusMessage.value = "No user session found"
        }
    }



    fun fetchAllUsers() {
        viewModelScope.launch {
            try {
                _users.value = api.getAllUsers()
            } catch (e: Exception) {
                _statusMessage.value = "🚨 Error fetching users: ${e.localizedMessage}"
            }
        }
    }

    fun fetchUserById(id: Int) {
        viewModelScope.launch {
            try {
                _selectedUser.value = api.getUserById(id)
            } catch (e: Exception) {
                _statusMessage.value = "🚨 Error fetching user: ${e.localizedMessage}"
            }
        }
    }

    fun updateUser(id: Int, user: User) {
        viewModelScope.launch {
            try {
                val response = api.updateUser(id, user)
                if (response.isSuccessful) {
                    _statusMessage.value = "✅ User updated"
                    fetchAllUsers()
                } else {
                    _statusMessage.value = "❌ Update failed: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "🚨 Error updating user: ${e.localizedMessage}"
            }
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            try {
                val response = api.deleteUser(id)
                if (response.isSuccessful) {
                    _statusMessage.value = "🗑️ User deleted"
                    fetchAllUsers()
                } else {
                    _statusMessage.value = "❌ Delete failed: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "🚨 Error deleting user: ${e.localizedMessage}"
            }
        }
    }

    fun clearSelectedUser() {
        _selectedUser.value = null
    }

    //login user
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = api.loginUser(LoginRequest(email, password))
                if (response.status == "success" && response.user != null) {
                    sessionManager.saveUserSession(
                        userId = response.user.id ?: 0,
                        fullName = response.user.full_name,
                        email = response.user.email,
                        phoneNumber = response.user.phone_number
                    )
                    _statusMessage.value = "Login successful"
                } else {
                    _statusMessage.value = response.message ?: "Login failed"
                }
            } catch (e: Exception) {
                _statusMessage.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }


}
