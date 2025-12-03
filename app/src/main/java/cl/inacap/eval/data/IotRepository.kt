// data/IotRepository.kt
package cl.inacap.eval.data

import cl.inacap.eval.data.remote.IotApi
import cl.inacap.eval.data.remote.HttpClient
import cl.inacap.eval.data.remote.dto.IotDataResponse

class IotRepository(
    private val api: IotApi = HttpClient.iotApi
) {
    suspend fun getData(): Result<IotDataResponse> {
        return try {
            val response = api.getData()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
