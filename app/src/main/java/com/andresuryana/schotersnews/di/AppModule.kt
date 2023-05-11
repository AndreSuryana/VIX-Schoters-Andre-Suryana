package com.andresuryana.schotersnews.di

import android.content.Context
import androidx.room.Room
import com.andresuryana.schootersnews.BuildConfig
import com.andresuryana.schotersnews.data.repository.NewsRepository
import com.andresuryana.schotersnews.data.repository.NewsRepositoryImpl
import com.andresuryana.schotersnews.data.source.local.DBContracts
import com.andresuryana.schotersnews.data.source.local.SchotersNewsDatabase
import com.andresuryana.schotersnews.data.source.remote.NewsApi
import com.andresuryana.schotersnews.data.source.remote.interceptor.ErrorInterceptor
import com.andresuryana.schotersnews.data.source.remote.interceptor.HeaderInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSchotersNewsDatabase(@ApplicationContext context: Context): SchotersNewsDatabase =
        Room.databaseBuilder(context, SchotersNewsDatabase::class.java, DBContracts.DATABASE_NAME)
            .build()

    @Singleton
    @Provides
    fun provideNewsApiService(): NewsApi =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HeaderInterceptor())
                    .addInterceptor(ErrorInterceptor())
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
                        }
                    )
                    .build()
            )
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                        .create()
                )
            )
            .build()
            .create(NewsApi::class.java)

    @Singleton
    @Provides
    fun provideRepository(local: SchotersNewsDatabase, remote: NewsApi): NewsRepository =
        NewsRepositoryImpl(local, remote)
}