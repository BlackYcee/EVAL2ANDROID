package cl.inacap.eval.data.remote

import cl.inacap.eval.data.remote.dto.CreateUserRequest
import cl.inacap.eval.data.remote.dto.UserDto

class UserRepository(private val api: UserApi = HttpClient.userApi) {
    suspend fun createUser(request: CreateUserRequest) = api.createUser(request)
    suspend fun getUsers() = api.getUsers()
    suspend fun updateUser(id: Int, user: UserDto) = api.updateUser(id, user)
    suspend fun deleteUser(id: Int) = api.deleteUser(id)
}
