package com.example.newsapp.ui.saved

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.ui.home.NewsActivity
import com.example.newsapp.ui.home.NewsAdapter
import com.example.newsapp.ui.profile.ProfileActivity
import com.example.newsapp.utils.SavedArticlesManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class SavedActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var savedRecycler: RecyclerView
    private lateinit var emptyStateText: TextView
    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topBar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        savedRecycler = findViewById(R.id.savedRecycler)
        emptyStateText = findViewById(R.id.emptyStateText)
        savedRecycler.layoutManager = LinearLayoutManager(this)

        loadSavedArticles()

        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_bookmarks
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(android.content.Intent(this, NewsActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_search -> {
                    startActivity(android.content.Intent(this, com.example.newsapp.ui.search.SearchActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_bookmarks -> {
                    // Already on saved screen
                    true
                }
                R.id.nav_profile -> {
                    startActivity(android.content.Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun loadSavedArticles() {
        val savedArticles = SavedArticlesManager.getSavedArticles(this)
        if (savedArticles.isEmpty()) {
            emptyStateText.visibility = View.VISIBLE
            savedRecycler.visibility = View.GONE
        } else {
            emptyStateText.visibility = View.GONE
            savedRecycler.visibility = View.VISIBLE
            adapter = NewsAdapter(savedArticles)
            savedRecycler.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload saved articles when returning to this screen
        loadSavedArticles()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_header, menu)
        menu?.findItem(R.id.action_notifications)?.icon?.setTint(getColor(android.R.color.black))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_notifications -> {
                Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
