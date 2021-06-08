package com.example.movie.di

import android.content.ContentValues
import android.util.Log
import com.example.movie.network.ApiInterface
import com.example.movie.untils.Constants
import com.example.movie.untils.Constants.Companion.BASE_URL
import com.example.movie.untils.Constants.Companion.TAG
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

// Singleton, Provides 쓸때 private 말고 public 으로 해야한다.
object NetworkModule {


    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        Log.d(ContentValues.TAG, "provideHttpClient: ")
        return OkHttpClient.Builder()
            //제한시간 15초
            .readTimeout(15, TimeUnit.SECONDS)
            //연결시간 15초
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()

    }


    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        Log.d(ContentValues.TAG, "provideRetrofitInstance: ")
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            //json 변화기 Factory
            .client(createOkHttpClient())
            .addConverterFactory(gsonConverterFactory)
            .build()

    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        Log.d(ContentValues.TAG, "provideConverterFactory: ")
        return GsonConverterFactory.create()
    }


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiInterface {
        Log.d(ContentValues.TAG, "provideApiService: ")
        return retrofit.create(ApiInterface::class.java)
    }


     private fun createOkHttpClient(): OkHttpClient {
         Log.d(TAG, "createOkHttpClient: ")
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(interceptor)
        return builder.build()
    }


}