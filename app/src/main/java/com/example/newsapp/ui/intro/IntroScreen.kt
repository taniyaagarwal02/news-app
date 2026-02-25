package com.example.newsapp.ui.intro



import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newsapp.R

import kotlinx.coroutines.delay
import androidx.compose.material3.Text
//import androidx.compose.ui.R
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun IntroScreen(onFinished: () -> Unit) {

    var startMorph by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val globeRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    val globeScale by animateFloatAsState(
        targetValue = if (startMorph) 0.3f else 1f,
        animationSpec = tween(1500),
        label = ""
    )

    val globeAlpha by animateFloatAsState(
        targetValue = if (startMorph) 0f else 1f,
        animationSpec = tween(1200),
        label = ""
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (startMorph) 1f else 0f,
        animationSpec = tween(1500),
        label = ""
    )

    val textScale by animateFloatAsState(
        targetValue = if (startMorph) 1f else 0.7f,
        animationSpec = tween(1500),
        label = ""
    )

    LaunchedEffect(Unit) {
        delay(3000)
        startMorph = true
        delay(2500)
        onFinished()   // 🔥 directly call callback
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(R.drawable.globe),
            contentDescription = null,
            modifier = Modifier
                .size(170.dp)
                .graphicsLayer {
                    rotationZ = globeRotation
                    rotationX = 15f
                    scaleX = globeScale
                    scaleY = globeScale
                }
                .alpha(globeAlpha)
        )

        val infiniteTransition = rememberInfiniteTransition(label = "")

        val shineOffset by infiniteTransition.animateFloat(
            initialValue = -300f,
            targetValue = 600f,
            animationSpec = infiniteRepeatable(
                animation = tween(2500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = ""
        )

        Text(
            text = "DAILY\nNEWS",
            fontSize = 48.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            lineHeight = 44.sp,
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0D47A1)
                        , // deep blue
                        Color.White,
                        Color(0xFF0D47A1)

                    ),
                    start = Offset(shineOffset, shineOffset),
                    end = Offset(shineOffset + 200f, shineOffset + 200f)
                ),
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.5f),
                    offset = Offset(2f, 4f),
                    blurRadius = 10f
                )
            ),
            modifier = Modifier
                .graphicsLayer {
                    scaleX = textScale
                    scaleY = textScale
                }
                .alpha(textAlpha)
        )


    }
}
