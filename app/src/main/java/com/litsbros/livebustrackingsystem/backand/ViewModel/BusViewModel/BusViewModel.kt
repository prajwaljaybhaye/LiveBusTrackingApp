package com.litsbros.livebustrackingsystem.backand.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.litsbros.livebustrackingsystem.backand.RetrofitClient
import com.litsbros.livebustrackingsystem.backand.model.BusModel
import com.litsbros.livebustrackingsystem.user.model.BusInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BusViewModel : ViewModel() {

    private val _buses = mutableStateListOf<BusModel>()
    val buses: List<BusModel> get() = _buses


    private val _searchQuery = mutableStateOf("")
    val searchQuery: String get() = _searchQuery.value

    val filteredBuses: List<BusModel>
        get() = if (_searchQuery.value.isBlank()) buses
        else buses.filter {
            it.bus_number.contains(_searchQuery.value, ignoreCase = true) ||
                    it.registration_number.contains(_searchQuery.value, ignoreCase = true) ||
                    it.model.contains(_searchQuery.value, ignoreCase = true)
        }

    fun updateSearch(query: String) {
        _searchQuery.value = query
    }

    init {
        fetchBuses()
        fetchAllBusInfos()
    }



    fun fetchBuses() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getAllBuses()
                if (response.isSuccessful) {
                    response.body()?.let { busList ->
                        withContext(Dispatchers.Main) {
                            _buses.clear()
                            _buses.addAll(busList)
                            Log.d("BusViewModel", "Fetched ${busList.size} buses")
                        }
                    }
                } else {
                    Log.e("BusViewModel", "API error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("BusViewModel", "Network error: ${e.message}", e)
            }
        }
    }

    fun addBus(bus: BusModel, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.addBus(bus)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Log.d("BusViewModel", "Bus added: ${response.body()}")
                        onSuccess()
                        fetchBuses()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        val errorMsg = "API error: ${response.code()} ${response.message()}"
                        Log.e("BusViewModel", errorMsg)
                        onError(errorMsg)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val errorMsg = "Network error: ${e.message}"
                    Log.e("BusViewModel", errorMsg, e)
                    onError(errorMsg)
                }
            }
        }
    }

    //bus info
    //bus info
    private val _busesInfo = mutableStateListOf<BusInfo>()
    val busesInfo: List<BusInfo> get() = _busesInfo

    fun fetchBusInfoByDriverId(driverId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val busList = RetrofitClient.api.getBusInfoByDriverId(driverId)
                withContext(Dispatchers.Main) {
                    // Fix: Clear and add to the correct list (_busesInfo)
                    _busesInfo.clear()
                    _busesInfo.addAll(busList)
                    Log.d("BusViewModel", "Fetched ${busList.size} buses for driverId=$driverId")
                }
            } catch (e: Exception) {
                Log.e("BusViewModel", "Error fetching by driverId: ${e.message}", e)
            }
        }
    }

    fun fetchBusInfoByBusId(busId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val busList = RetrofitClient.api.getBusInfoByBusId(busId)
                withContext(Dispatchers.Main) {
                    _busesInfo.clear()
                    _busesInfo.addAll(busList)
                    Log.d("BusViewModel", "Fetched ${busList.size} buses for busId=$busId")
                }
            } catch (e: Exception) {
                Log.e("BusViewModel", "Error fetching by busId: ${e.message}", e)
            }
        }
    }

    fun clearBusInfo() {
        _busesInfo.clear()
    }


    fun fetchAllBusInfos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val busList = RetrofitClient.api.getAllBusInfos()
                withContext(Dispatchers.Main) {
                    // Fix: Clear and add to the correct list (_busesInfo)
                    _busesInfo.clear()
                    _busesInfo.addAll(busList)
                    Log.d("BusViewModel", "Fetched ${busList.size} total buses")
                }
            } catch (e: Exception) {
                Log.e("BusViewModel", "Error fetching all buses: ${e.message}", e)
            }
        }
    }


}
