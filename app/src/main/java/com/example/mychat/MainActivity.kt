package com.example.mychat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mychat.view.ChatScreen
import com.example.mychat.view.EmailScreen
import com.example.mychat.view.OTPScreen
import com.example.mychat.view.PasswordScreen
import com.example.mychat.view.ProfileScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val controller = rememberNavController()
            NavHost(controller, startDestination = "email"){
                composable("email") {
                    EmailScreen(controller)
                }
                composable("otp") {
                    OTPScreen(controller)
                }
                composable("password") {
                    PasswordScreen(controller)
                }
                composable("profile") {
                    ProfileScreen(controller)
                }
                composable("chat") {
                    ChatScreen()
                }
            }
        }
    }
}

