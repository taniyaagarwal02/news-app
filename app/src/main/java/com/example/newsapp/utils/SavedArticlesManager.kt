package com.example.newsapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.newsapp.data.model.Article
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SavedArticlesManager {
    private const val PREFS_NAME = "saved_articles"
    private const val KEY_ARTICLES = "articles"
    private val gson = Gson()

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveArticle(context: Context, article: Article): Boolean {
        val articles = getSavedArticles(context).toMutableList()
        // Check if article already exists (by URL)
        if (articles.any { it.url == article.url }) {
            return false // Already saved
        }
        articles.add(article)
        return saveArticles(context, articles)
    }

    fun removeArticle(context: Context, article: Article): Boolean {
        val articles = getSavedArticles(context).toMutableList()
        val removed = articles.removeAll { it.url == article.url }
        if (removed) {
            saveArticles(context, articles)
        }
        return removed
    }

    fun isArticleSaved(context: Context, url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        return getSavedArticles(context).any { it.url == url }
    }

    fun getSavedArticles(context: Context): List<Article> {
        val json = getSharedPreferences(context).getString(KEY_ARTICLES, null)
        if (json.isNullOrBlank()) return emptyList()
        return try {
            val type = object : TypeToken<List<Article>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveArticles(context: Context, articles: List<Article>): Boolean {
        return try {
            val json = gson.toJson(articles)
            getSharedPreferences(context).edit().putString(KEY_ARTICLES, json).apply()
            true
        } catch (e: Exception) {
            false
        }
    }
}
