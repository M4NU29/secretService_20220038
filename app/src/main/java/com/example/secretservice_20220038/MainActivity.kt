package com.example.secretservice_20220038

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.secretservice_20220038.App.Companion.realm
import com.example.secretservice_20220038.ui.theme.SecretService_20220038Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val realm = realm
        setContent {
            SecretService_20220038Theme {
                HomeScreen(realm)
            }
        }
    }
}
