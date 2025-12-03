package cl.inacap.eval.data.remote.dto

data class UserDto(
    val id: Int? = null,
    val name: String,
    val last_name: String? = null,
    val email: String,
    val createdAt: String? = null
)
