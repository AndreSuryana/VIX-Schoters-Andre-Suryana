package com.andresuryana.schotersnews.data.repository

import com.andresuryana.schotersnews.data.model.News
import com.andresuryana.schotersnews.data.model.ResponseWrapper
import com.andresuryana.schotersnews.util.NewsCategory
import com.andresuryana.schotersnews.util.Resource

interface NewsRepository {

    suspend fun getHeadlines(): Resource<ResponseWrapper>

    suspend fun getExplore(category: NewsCategory = NewsCategory.values().first()): Resource<ResponseWrapper>

    suspend fun searchNews(keyword: String): Resource<ResponseWrapper>

    suspend fun bookmarkNews(news: News): Resource<Boolean>

    suspend fun removeBookmarkedNews(news: News): Resource<Boolean>

    suspend fun getBookmarkedNews(): Resource<List<News>>

    suspend fun isNewsBookmarked(news: News): Resource<Boolean>
}