package com.example.newsapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.utils.API_KEY
import com.example.newsapp.utils.COUNTRY_CODE
import com.example.newsapp.viewmodel.NewsViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.graphics.drawable.GradientDrawable
import com.example.newsapp.ui.search.SearchActivity

class NewsActivity : AppCompatActivity() {

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var breakingNewsRecycler: RecyclerView
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var dotIndicators: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.newsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        breakingNewsRecycler = findViewById(R.id.breakingNewsRecycler)
        breakingNewsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        dotIndicators = findViewById(R.id.dotIndicators)
        shimmerLayout = findViewById(R.id.shimmerLayout)
        bottomNav = findViewById(R.id.bottomNav)

        breakingNewsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                updateDotIndicators()
            }
        })

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topBar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            Toast.makeText(this, "Menu clicked", Toast.LENGTH_SHORT).show()
        }

        bottomNav.selectedItemId = R.id.nav_home
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    if (::recyclerView.isInitialized) recyclerView.smoothScrollToPosition(0)
                    true
                }
                R.id.nav_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    true
                }

                R.id.nav_bookmarks -> {
                    startActivity(Intent(this, com.example.newsapp.ui.saved.SavedActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, com.example.newsapp.ui.profile.ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        showShimmer()
        swipeRefresh.setOnRefreshListener {
            viewModel.getTopHeadlines(COUNTRY_CODE, API_KEY)
        }
        viewModel.getTopHeadlines(COUNTRY_CODE, API_KEY)

        viewModel.newsResponse.observe(this) { response ->
            if (response != null && response.isSuccessful) {
                val articles = response.body()?.articles ?: emptyList()
                swipeRefresh.isRefreshing = false
                recyclerView.adapter = NewsAdapter(articles)
                val breaking = articles.take(5)
                breakingNewsRecycler.adapter = BreakingNewsAdapter(breaking)
                setupDotIndicators(breaking.size)
                hideShimmer()
            }
        }
    }

    private fun setupDotIndicators(count: Int) {
        dotIndicators.removeAllViews()
        val dotSize = (8 * resources.displayMetrics.density).toInt()
        for (i in 0 until count) {
            val dot = View(this)
            val params = LinearLayout.LayoutParams(dotSize, dotSize)
            val margin = (4 * resources.displayMetrics.density).toInt()
            params.setMargins(margin, 0, margin, 0)
            dot.layoutParams = params
            val drawable = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(getColor(if (i == 0) R.color.dot_active else R.color.dot_inactive))
            }
            dot.background = drawable
            dotIndicators.addView(dot)
        }
    }

    private fun updateDotIndicators() {
        val layoutManager = breakingNewsRecycler.layoutManager as? LinearLayoutManager
        val firstVisiblePosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
        val itemCount = breakingNewsRecycler.adapter?.itemCount ?: 0
        for (i in 0 until dotIndicators.childCount) {
            val dot = dotIndicators.getChildAt(i)
            val drawable = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                val isActive = i == firstVisiblePosition && firstVisiblePosition < itemCount
                setColor(getColor(if (isActive) R.color.dot_active else R.color.dot_inactive))
            }
            dot.background = drawable
        }
    }

    private fun showShimmer() {
        shimmerLayout.visibility = View.VISIBLE
        shimmerLayout.startShimmer()
        recyclerView.visibility = View.GONE
    }

    private fun hideShimmer() {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_header, menu)
        menu?.findItem(R.id.action_notifications)?.icon?.setTint(getColor(android.R.color.black))
        menu?.findItem(R.id.action_search)?.icon?.setTint(getColor(android.R.color.black))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, com.example.newsapp.ui.search.SearchActivity::class.java))
                return true
            }
            R.id.action_notifications -> {
                Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
