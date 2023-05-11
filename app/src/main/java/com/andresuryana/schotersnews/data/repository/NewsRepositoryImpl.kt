package com.andresuryana.schotersnews.data.repository

import com.andresuryana.schotersnews.data.model.News
import com.andresuryana.schotersnews.data.model.ResponseWrapper
import com.andresuryana.schotersnews.data.source.local.SchotersNewsDatabase
import com.andresuryana.schotersnews.data.source.remote.NewsApi
import com.andresuryana.schotersnews.util.Ext.parseErrorResponse
import com.andresuryana.schotersnews.util.NewsCategory
import com.andresuryana.schotersnews.util.Resource
import com.andresuryana.schotersnews.util.SortByConstants
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val local: SchotersNewsDatabase,
    private val remote: NewsApi
) : NewsRepository {

    override suspend fun getHeadlines(): Resource<ResponseWrapper> {
        return try {
            val response = remote.getTopHeadlines(pageSize = 25)
            val result = response.body()

            if (response.isSuccessful && result!!.status == "ok") {
                Resource.Success(result)
            } else {
                Resource.Failure(response.parseErrorResponse().message)
            }
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }

    override suspend fun getExplore(category: NewsCategory): Resource<ResponseWrapper> {
        return try {
            val response = remote.getAllNews(
                keyword = category.keyword,
                sortBy = SortByConstants.RELEVANCY,
                pageSize = 25
            )
            val result = response.body()

            if (response.isSuccessful && result!!.status == "ok") {
                Resource.Success(result)
            } else {
                Resource.Failure(response.parseErrorResponse().message)
            }
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }

    override suspend fun searchNews(keyword: String): Resource<ResponseWrapper> {
        return try {
            val response = remote.getAllNews(
                keyword = keyword,
                sortBy = SortByConstants.RELEVANCY,
                pageSize = 100
            )
            val result = response.body()

            if (response.isSuccessful && result!!.status == "ok") {
                Resource.Success(result)
            } else {
                Resource.Failure(response.parseErrorResponse().message)
            }
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }

    override suspend fun bookmarkNews(news: News): Resource<Boolean> {
        return try {
            local.bookmarkDao().insert(news.toNewsEntity())
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }

    override suspend fun removeBookmarkedNews(news: News): Resource<Boolean> {
        return try {
            local.bookmarkDao().remove(news.newsUrl)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }

    override suspend fun getBookmarkedNews(): Resource<List<News>> {
        return try {
            val result = local.bookmarkDao().getAllNews()
            Resource.Success(result.map {
                it.toNews()
            })
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }

    override suspend fun isNewsBookmarked(news: News): Resource<Boolean> {
        return try {
            val result = local.bookmarkDao().isNewsBookmarked(news.newsUrl)
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }
}