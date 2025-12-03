package cl.inacap.eval.data

import android.content.Context
import cl.inacap.eval.data.local.TokenStorage
import cl.inacap.eval.data.remote.AuthApi
import cl.inacap.eval.data.remote.HttpClient
import cl.inacap.eval.data.remote.dto.LoginRequest
import cl.inacap.eval.data.remote.dto.LoginResponse
import cl.inacap.eval.data.remote.dto.RegisterRequest
import cl.inacap.eval.data.remote.dto.RegisterResponse
import cl.inacap.eval.data.remote.dto.UserDto
import cl.inacap.eval.data.remote.dto.ForgotPasswordRequest
import cl.inacap.eval.data.remote.dto.ForgotPasswordResponse
import cl.inacap.eval.data.remote.dto.ResetPasswordRequest
import cl.inacap.eval.data.remote.dto.ResetPasswordResponse

class AuthRepository(
    private val api: AuthApi = HttpClient.authApi
) {

    // ---------- REGISTER ----------
    suspend fun register(
        name: String,
        lastName: String,
        email: String,
        password: String
    ): Result<RegisterResponse> {
        return try {
            val body = RegisterRequest(name, lastName, email, password)
            val response = api.register(body)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun checkEmailExists(email: String): Boolean {
        // Aquí consultas tu BD o API para ver si el correo ya existe
        // Ejemplo simulado:
        val existingEmails = listOf("test@inacap.cl", "admin@inacap.cl")
        return existingEmails.contains(email)
    }

    // ---------- LOGIN ----------
    suspend fun login(ctx: Context, email: String, password: String): Result<LoginResponse> {
        return try {
            val body = LoginRequest(email = email, password = password)
            val response = api.login(body)
            if (!response.success) {
                return Result.failure(Exception("Credenciales inválidas"))
            }
            TokenStorage.saveToken(ctx, response.token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------- TOKEN ----------
    suspend fun getStoredToken(ctx: Context): String? {
        return TokenStorage.getToken(ctx)
    }

    suspend fun clearToken(ctx: Context) {
        TokenStorage.clearToken(ctx)
    }

    // ---------- VALIDAR TOKEN (GET /profile) ----------
    suspend fun validateToken(ctx: Context): Result<UserDto> {
        return try {
            val token = getStoredToken(ctx)
                ?: return Result.failure(Exception("Sin token guardado"))

            val user = api.profile("Bearer $token")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------- FORGOT PASSWORD ----------
    suspend fun forgotPassword(ctx: Context, req: ForgotPasswordRequest): ForgotPasswordResponse {
        return api.forgotPassword(req)
    }

    // ---------- RESET PASSWORD ----------
    suspend fun resetPassword(ctx: Context, req: ResetPasswordRequest): ResetPasswordResponse {
        return api.resetPassword(req)
    }

}
