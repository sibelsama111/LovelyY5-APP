package com.sibelsama.lovelyy5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sibelsama.lovelyy5.ui.screens.NavGraph
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LovelyY5APPTheme {
                NavGraph()
            }
        }
    }
}
