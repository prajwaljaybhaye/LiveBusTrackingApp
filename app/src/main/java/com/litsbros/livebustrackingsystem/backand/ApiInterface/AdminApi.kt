package com.litsbros.livebustrackingsystem.backand.ApiInterface

import com.litsbros.livebustrackingsystem.admin.model.Bus
import com.litsbros.livebustrackingsystem.admin.model.Driver
import com.litsbros.livebustrackingsystem.admin.model.Route
import com.litsbros.livebustrackingsystem.backand.model.AdminLogin
import com.litsbros.livebustrackingsystem.backand.model.AdminLoginResponse
import com.litsbros.livebustrackingsystem.backand.model.BusModel
import com.litsbros.livebustrackingsystem.backand.model.DriverAuth
import com.litsbros.livebustrackingsystem.backand.model.DriverModel
import com.litsbros.livebustrackingsystem.backand.model.RouteModel
import com.litsbros.livebustrackingsystem.backand.model.DriverLocationPayload
import com.litsbros.livebustrackingsystem.backand.model.DriverLoginResponse
import com.litsbros.livebustrackingsystem.backand.model.LiveLocationResponse
import com.litsbros.livebustrackingsystem.backand.model.LoginRequest
import com.litsbros.livebustrackingsystem.backand.model.LoginResponse
import com.litsbros.livebustrackingsystem.backand.model.RegistrationResponse
import com.litsbros.livebustrackingsystem.backand.model.User
import com.litsbros.livebustrackingsystem.user.model.BusInfo
import retrofit2.Response
import retrofit2.http.*

interface AdminApi {

    // -------------------- Bus --------------------
    @GET("buses_api.php")
    suspend fun getAllBuses(): Response<List<BusModel>>

    @GET("buses_api.php")
    suspend fun getBusById(@Query("id") id: Int): Response<BusModel>

    @Headers("Content-Type: application/json")
    @POST("buses_api.php")
    suspend fun addBus(@Body bus: BusModel): Response<BusModel>

    @Headers("Content-Type: application/json")
    @PUT("buses_api.php")
    suspend fun updateBus(@Query("id") id: Int, @Body bus: BusModel): Response<BusModel>

    @DELETE("buses_api.php")
    suspend fun deleteBus(@Query("id") id: Int): Response<Unit>

    // -------------------- Driver --------------------
    @GET("drivers_api.php")
    suspend fun getAllDrivers(): Response<List<DriverModel>>


    @GET("drivers_api.php")
    suspend fun getDriverById(@Query("id") id: Int): Response<DriverModel>

    @Headers("Content-Type: application/json")
    @POST("drivers_api.php")
    suspend fun addDriver(@Body driver: DriverModel): Response<DriverModel>

    @Headers("Content-Type: application/json")
    @PUT("drivers_api.php")
    suspend fun updateDriver(@Query("id") id: Int, @Body driver: DriverModel): Response<DriverModel>

    @DELETE("drivers_api.php")
    suspend fun deleteDriver(@Query("id") id: Int): Response<Unit>

    // -------------------- Route --------------------
    @GET("routes_api.php")
    suspend fun getAllRoutes(): Response<List<RouteModel>>

    @GET("routes_api.php")
    suspend fun getRouteById(@Query("id") id: Int): Response<RouteModel>

    @Headers("Content-Type: application/json")
    @POST("routes_api.php")
    suspend fun addRoute(@Body route: RouteModel): Response<RouteModel>

    @Headers("Content-Type: application/json")
    @PUT("routes_api.php")
    suspend fun updateRoute(@Query("id") id: Int, @Body route: RouteModel): Response<RouteModel>

    @DELETE("routes_api.php")
    suspend fun deleteRoute(@Query("id") id: Int): Response<Unit>

    //      Driver Login
    @Headers("Content-Type: application/json")
    @POST("driver_login.php")
    suspend fun driverLogin(@Body auth: DriverAuth): Response<DriverLoginResponse>


    @POST("live_locations.php")
    suspend fun updateLocation(
        @Body payload: DriverLocationPayload
    ): Response<Unit>


    @GET("live_locations.php")
    suspend fun getLiveLocationByDriver(
        @Query("driver_id") driverId: Int
    ): Response<LiveLocationResponse>


    @GET("live_locations.php")
    suspend fun getLiveLocationByBus(
        @Query("bus_id") busId: Int
    ): Response<LiveLocationResponse>




    @GET("live_locations.php?all=true")
    suspend fun getAllLiveLocations(): Response<List<LiveLocationResponse>>


    //bus info

    @GET("bus_info.php")
    suspend fun getAllBusInfos(): List<BusInfo>

    @GET("bus_info.php")
    suspend fun getBusInfoByDriverId(
        @Query("driver_id") driverId: Int
    ): List<BusInfo>


    @GET("bus_info.php")
    suspend fun getBusInfoByBusId(@Query("bus_id") busId: Int): List<BusInfo>





    //--------------------user start -----------------------------------



    @POST("users_api.php")
    suspend fun createUser(@Body user: User): RegistrationResponse

    @GET("users_api.php")
    suspend fun getAllUsers(): List<User>

    @GET("users_api.php")
    suspend fun getUserById(@Query("id") id: Int): User

    @PUT("users_api.php")
    suspend fun updateUser(@Query("id") id: Int, @Body user: User): Response<Map<String, Any>>

    @DELETE("users_api.php")
    suspend fun deleteUser(@Query("id") id: Int): Response<Map<String, Any>>


    //login user
    @POST("user_login.php")
    suspend fun loginUser(@Body credentials: LoginRequest): LoginResponse

    //admin login
    @POST("admin_login.php")
    suspend fun loginAdmin(@Body request: AdminLogin): AdminLoginResponse



}
