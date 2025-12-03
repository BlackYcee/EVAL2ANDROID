package cl.inacap.eval.data.remote

import cl.inacap.eval.data.remote.dto.ForgotPasswordRequest
import cl.inacap.eval.data.remote.dto.ForgotPasswordResponse
import cl.inacap.eval.data.remote.dto.LoginRequest
import cl.inacap.eval.data.remote.dto.LoginResponse
import cl.inacap.eval.data.remote.dto.RegisterRequest
import cl.inacap.eval.data.remote.dto.RegisterResponse
import cl.inacap.eval.data.remote.dto.ResetPasswordRequest
import cl.inacap.eval.data.remote.dto.ResetPasswordResponse
import cl.inacap.eval.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): ForgotPasswordResponse

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body req: ResetPasswordRequest): ResetPasswordResponse

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @GET("profile")
    suspend fun profile(
        @Header("Authorization") auth: String



    ): UserDto
}

