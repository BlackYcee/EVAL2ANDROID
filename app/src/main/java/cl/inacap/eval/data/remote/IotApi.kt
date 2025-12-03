// data/remote/IotApi.kt
package cl.inacap.eval.data.remote

import cl.inacap.eval.data.remote.dto.IotDataResponse
import retrofit2.http.GET

interface IotApi {
    @GET("iot/data")
    suspend fun getData(): IotDataResponse
}
