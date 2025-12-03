package cl.inacap.eval.data.remote.dto


data class IotDataResponse(
    val temperature: Double,
    val humidity: Double,
    val timestamp: String
)
