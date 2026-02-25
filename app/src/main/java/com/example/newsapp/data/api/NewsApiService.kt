package com.example.newsapp.data.api

import com.example.newsapp.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apikey") apiKey: String
    ): Response<NewsResponse>

    @GET("search")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("apikey") apiKey: String
    ): Response<NewsResponse>


}
