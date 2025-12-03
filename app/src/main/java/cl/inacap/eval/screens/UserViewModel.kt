package cl.inacap.eval.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.inacap.eval.data.remote.UserRepository
import cl.inacap.eval.data.remote.dto.CreateUserRequest
import cl.inacap.eval.data.remote.dto.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val repo = UserRepository() // usa el HttpClient por defecto

    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users: StateFlow<List<UserDto>> = _users

    fun loadUsers() {
        viewModelScope.launch {
            val response = repo.getUsers()
            if (response.isSuccessful) {
                _users.value = response.body() ?: emptyList()
            }
        }
    }

    // UserViewModel.kt
    fun insertUser(request: CreateUserRequest) {
        viewModelScope.launch {
            repo.createUser(request)
            loadUsers()
        }
    }


    fun updateUser(id: Int, user: UserDto) {
        viewModelScope.launch {
            repo.updateUser(id, user)
            loadUsers()
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            repo.deleteUser(id)
            loadUsers()
        }
    }
}
