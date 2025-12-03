package cl.inacap.eval.screens.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.inacap.eval.data.AuthRepository
import cl.inacap.eval.data.local.TokenStorage
import cl.inacap.eval.data.remote.dto.UserDto
import cl.inacap.eval.data.remote.dto.ForgotPasswordRequest
import cl.inacap.eval.data.remote.dto.ResetPasswordRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Estados de autenticación
sealed class AuthState {
    data object Checking : AuthState()         // Splash chequeando
    data object Unauthenticated : AuthState()  // Ir a Login
    data class Authenticated(val user: UserDto) : AuthState() // Ir a Home
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    application: Application,
    private val repo: AuthRepository = AuthRepository()
) : AndroidViewModel(application) {

    constructor(application: Application) : this(application, AuthRepository())

    private val _authState = MutableStateFlow<AuthState>(AuthState.Checking)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkSession()
    }

    private fun appContext() = getApplication<Application>().applicationContext

    fun checkSession() {
        viewModelScope.launch {
            val ctx = appContext()
            val token = repo.getStoredToken(ctx)
            if (token.isNullOrEmpty()) {
                _authState.value = AuthState.Unauthenticated
                return@launch
            }

            val res = repo.validateToken(ctx)
            _authState.value = res.fold(
                onSuccess = { user -> AuthState.Authenticated(user) },
                onFailure = { AuthState.Unauthenticated }
            )
        }
    }

    fun login(email: String, pass: String) {
        _authState.value = AuthState.Checking
        viewModelScope.launch {
            val ctx = appContext()
            val res = repo.login(ctx, email, pass)

            _authState.value = res.fold(
                onSuccess = { AuthState.Authenticated(it.user) },
                onFailure = {
                    AuthState.Error(it.message ?: "Error al iniciar sesión. Intente nuevamente.")
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            TokenStorage.clearToken(appContext())
            _authState.value = AuthState.Unauthenticated
        }
    }


    fun forgotPassword(
        email: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val ctx = appContext()
                val res = repo.forgotPassword(ctx, ForgotPasswordRequest(email))
                onSuccess(res.code) // devuelve el código
            } catch (e: Exception) {
                onError(e.message ?: "Error al solicitar recuperación")
            }
        }
    }


    fun resetPassword(
        email: String,
        code: String,
        newPassword: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val ctx = appContext()
                val res = repo.resetPassword(ctx, ResetPasswordRequest(email, code, newPassword))
                onSuccess(res.message)
            } catch (e: Exception) {
                onError(e.message ?: "Error al cambiar contraseña")
            }
        }
    }

}
