package cl.inacap.eval.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object HttpClient {
    private const val BASE_URL = "http://10.0.2.2:3000/"

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttp: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttp)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)

    val iotApi: IotApi by lazy {
        retrofit.create(IotApi::class.java)
    }

    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }
}
