package com.example.newsapp.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.ui.home.NewsAdapter
import com.example.newsapp.utils.API_KEY
import com.example.newsapp.viewmodel.NewsViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import com.example.newsapp.ui.home.NewsActivity
import com.example.newsapp.ui.saved.SavedActivity
import com.example.newsapp.ui.profile.ProfileActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast


class SearchActivity : AppCompatActivity() {

    private val viewModel: NewsViewModel by viewModels()

    private lateinit var searchEditText: TextInputEditText
    private lateinit var recycler: RecyclerView
    private lateinit var loader: ProgressBar
    private lateinit var toolbar: MaterialToolbar
    private lateinit var emptyStateText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        toolbar = findViewById(R.id.searchToolbar)
        setSupportActionBar(toolbar)

        searchEditText = findViewById(R.id.searchEditText)
        recycler = findViewById(R.id.searchRecycler)
        loader = findViewById(R.id.searchLoader)
        emptyStateText = findViewById(R.id.emptyStateText)

        recycler.layoutManager = LinearLayoutManager(this)

//        toolbar.setNavigationOnClickListener { finish() }
        toolbar.setNavigationOnClickListener {
            Toast.makeText(this, "Menu clicked", Toast.LENGTH_SHORT).show()
        }


        // Initialize with empty adapter
        recycler.adapter = NewsAdapter(emptyList())

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()

                if (query.length > 2) {
                    emptyStateText.visibility = View.GONE
                    loader.visibility = View.VISIBLE
                    recycler.visibility = View.VISIBLE
                    viewModel.searchNews(query, API_KEY)
                } else if (query.isEmpty()) {
                    emptyStateText.visibility = View.VISIBLE
                    emptyStateText.text = "Search for news articles..."
                    recycler.adapter = NewsAdapter(emptyList())
                    loader.visibility = View.GONE
                } else {
                    emptyStateText.visibility = View.VISIBLE
                    emptyStateText.text = "Type at least 3 characters to search"
                    recycler.adapter = NewsAdapter(emptyList())
                    loader.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        viewModel.newsResponse.observe(this) { response ->
            loader.visibility = View.GONE

            if (response != null && response.isSuccessful) {
                val articles = response.body()?.articles ?: emptyList()

                if (articles.isEmpty()) {
                    emptyStateText.visibility = View.VISIBLE
                    emptyStateText.text = "No articles found. Try a different search term."
                    recycler.adapter = NewsAdapter(emptyList())
                } else {
                    emptyStateText.visibility = View.GONE
                    recycler.adapter = NewsAdapter(articles)
                }
            } else {
                emptyStateText.visibility = View.VISIBLE
                emptyStateText.text = "Error loading results. Please try again."
                recycler.adapter = NewsAdapter(emptyList())
            }
        }
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.selectedItemId = R.id.nav_search

        bottomNav.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_home -> {
                    startActivity(Intent(this, NewsActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_search -> true

                R.id.nav_bookmarks -> {
                    startActivity(Intent(this, SavedActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_header, menu)
        menu?.findItem(R.id.action_notifications)?.icon?.setTint(getColor(android.R.color.black))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_notifications) {
            Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
