package cl.inacap.eval.data.remote.dto

// data/remote/dto/AuthDto.kt
data class LoginRequest(
    val email: String,
    val password: String
)


data class CreateUserRequest(
    val name: String,
    val last_name: String? = null,
    val email: String,
    val password_hash: String
)

data class LoginResponse(
    val success: Boolean,
    val token: String,
    val user: UserDto
)

data class RegisterRequest(
    val name: String,
    val last_name: String,
    val email: String,
    val password: String
)

// data/remote/dto/RegisterResponse.kt
data class RegisterResponse(
    val message: String,
    val user: UserDto
)

data class ForgotPasswordRequest(
    val email: String
)

data class ForgotPasswordResponse(
    val code: String
)

data class ResetPasswordRequest(
    val email: String,
    val code: String,
    val new_password: String
)

data class ResetPasswordResponse(
    val message: String
)
