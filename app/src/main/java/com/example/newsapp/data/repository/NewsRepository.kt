package com.example.newsapp.data.repository

import com.example.newsapp.data.api.RetrofitInstance

class NewsRepository {

    suspend fun getTopHeadlines(country: String, apiKey: String) =
        RetrofitInstance.api.getTopHeadlines(country, apiKey)

    suspend fun searchNews(query: String, apiKey: String) =
        RetrofitInstance.api.searchNews(query, apiKey)

}
