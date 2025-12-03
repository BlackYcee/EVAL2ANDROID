package cl.inacap.eval.data.remote

import cl.inacap.eval.data.remote.dto.CreateUserRequest
import cl.inacap.eval.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @POST("users")
    suspend fun createUser(@Body request: CreateUserRequest): Response<UserDto>

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: UserDto): Response<UserDto>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>
}

