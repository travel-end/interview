package com.journey.interview.weatherapp.net

import android.util.Log
import com.journey.interview.weatherapp.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @By Journey 2020/9/15
 * @Description
 */
class RetrofitClient {
    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(30,TimeUnit.SECONDS)
        .writeTimeout(30,TimeUnit.SECONDS)
        .addNetworkInterceptor(getLoggerInterceptor())
        .build()



    class LoggerInterceptor: HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Log.e("RetrofitClient",message)
        }
    }

    private fun getLoggerInterceptor() :HttpLoggingInterceptor{
        val httpLoggingInterceptor = HttpLoggingInterceptor(LoggerInterceptor())
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    fun <T> createApiService(clazz:Class<T>):T {
        return retrofit.create(clazz)
    }

    companion object {
        val instance by lazy {
            RetrofitClient()
        }
    }
}