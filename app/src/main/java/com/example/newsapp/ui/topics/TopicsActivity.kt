package com.example.newsapp.ui.topics

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.ui.home.NewsActivity
import com.example.newsapp.ui.profile.ProfileActivity
import com.example.newsapp.ui.saved.SavedActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class TopicsActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var topicsRecycler: RecyclerView
    private lateinit var topicsAdapter: TopicsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topics)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topBar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Topics grid
        topicsRecycler = findViewById(R.id.topicsRecycler)
        topicsRecycler.layoutManager = GridLayoutManager(this, 3)

        val topicItems = listOf(
            TopicItem("Business"),
            TopicItem("Technology"),
            TopicItem("Science"),
            TopicItem("Sports"),
            TopicItem("Health"),
            TopicItem("Politics"),
            TopicItem("Entertainment"),
            TopicItem("Travel"),
            TopicItem("Food")
        )

        topicsAdapter = TopicsAdapter(topicItems) { selected ->
            // You can persist selected topics here later
        }
        topicsRecycler.adapter = topicsAdapter

        bottomNav = findViewById(R.id.bottomNav)
//        bottomNav.selectedItemId = R.id.nav_topics
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(android.content.Intent(this, NewsActivity::class.java))
                    finish()
                    true
                }
//                R.id.nav_topics -> {
//                    // Already on topics screen
//                    true
//                }
                R.id.nav_bookmarks -> {
                    startActivity(android.content.Intent(this, SavedActivity::class.java))
                    finish()
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
