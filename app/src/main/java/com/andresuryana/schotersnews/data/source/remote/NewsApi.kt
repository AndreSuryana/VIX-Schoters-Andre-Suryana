package com.andresuryana.schotersnews.data.source.remote

import com.andresuryana.schotersnews.data.model.ResponseWrapper
import com.andresuryana.schotersnews.util.SortByConstants.POPULARITY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("language") language: String = "en",
        @Query("pageSize") pageSize: Int = 25,
        @Query("page") page: Int = 1
    ): Response<ResponseWrapper>

    @GET("everything")
    suspend fun getAllNews(
        @Query("q") keyword: String,
        @Query("sortBy") sortBy: String = POPULARITY,
        @Query("language") language: String = "en",
        @Query("pageSize") pageSize: Int = 25,
        @Query("page") page: Int = 1
    ): Response<ResponseWrapper>
}