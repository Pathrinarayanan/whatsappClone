package com.example.mychat

import com.example.mychat.modal.OtpRequest
import com.example.mychat.modal.OtpResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("/send-otp")
    suspend fun sendOtp(@Body request : OtpRequest) : OtpResponse

    @Headers("Content-Type: application/json")
    @POST("/verify-otp")
    suspend fun verifyOtp(@Body request : OtpRequest): OtpResponse
}