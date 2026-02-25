package com.example.newsapp.ui.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.ui.home.NewsActivity

class IntroActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IntroScreen(
                onFinished = {
                    startActivity(Intent(this, NewsActivity::class.java))
                    finish()
                }
            )
        }
    }
}
