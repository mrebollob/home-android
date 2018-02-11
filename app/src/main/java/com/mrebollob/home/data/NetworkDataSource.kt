package com.mrebollob.home.data

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.mrebollob.home.domain.Status
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkDataSource {

  private val homeApi: HomeApi

  init {

    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
        .readTimeout(TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    homeApi = Retrofit.Builder()
        .baseUrl("http://192.168.0.176:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build().create(HomeApi::class.java)
  }

  fun openStreetDoor(): Observable<Status> {
    return homeApi.openStreetDoor()
  }

  companion object {
    private val TIMEOUT_IN_MS = 10000L
  }
}