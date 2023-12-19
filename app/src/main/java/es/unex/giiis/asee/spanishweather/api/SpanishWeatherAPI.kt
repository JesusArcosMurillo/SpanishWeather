package es.unex.giiis.asee.spanishweather.api
import es.unex.giiis.asee.spanishweather.api.clasesapi.Localidad
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private val service: SpanishWeatherAPI by lazy {

    val retrofit = Retrofit.Builder()
        .baseUrl("http://api.weatherapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(SpanishWeatherAPI::class.java)
}

fun conexionAPI() = service

interface SpanishWeatherAPI {

    @GET("current.json")
    fun getPronostico(
        @Query("key") apikey: String,
        @Query("q") ciudad: String,
        @Query("aqi") contaminacion: Boolean,
        @Query("days") diaspronostico: Int,
        @Query("alerts") alertas: Boolean,
    ): Call<Localidad>
}

