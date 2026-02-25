package com.example.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.model.NewsResponse
import com.example.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel : ViewModel() {

    private val repository = NewsRepository()

//    private val _newsResponse = MutableLiveData<Response<NewsResponse>>()
//    val newsResponse: LiveData<Response<NewsResponse>> = _newsResponse
private val _newsResponse = MutableLiveData<Response<NewsResponse>?>()
    val newsResponse: LiveData<Response<NewsResponse>?> = _newsResponse

    fun getTopHeadlines(country: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getTopHeadlines(country, apiKey)
                _newsResponse.value = response
            } catch (e: Exception) {
                _newsResponse.postValue(null)
            }
        }
    }
    fun searchNews(query: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.searchNews(query, apiKey)
                _newsResponse.postValue(response)
            } catch (e: Exception) {
                _newsResponse.postValue(null)
            }
        }
    }


}
