package es.unex.giiis.asee.spanishweather.api

import android.util.Log
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val service: SpanishWeatherAPI by lazy {

    val loggingInterceptor = HttpLoggingInterceptor { message -> Log.d("Retrofit", message) }.apply {
        level = HttpLoggingInterceptor.Level.BODY // Puedes cambiar el nivel según tus necesidades
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://api.weatherapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    retrofit.create(SpanishWeatherAPI::class.java)
}

fun conexionAPI() = service

interface SpanishWeatherAPI {

    @GET("forecast.json")
    suspend fun getPronostico(
        @Query("key") apikey: String,
        @Query("q") ciudad: String,
        @Query("aqi") contaminacion: String,
        @Query("days") diaspronostico: Int
    ): Localidad
}

class APIError(message: String, cause: Throwable?) : Throwable(message, cause)