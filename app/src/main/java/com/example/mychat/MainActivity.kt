package com.example.mychat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mychat.view.ChatScreen
import com.example.mychat.view.EmailScreen
import com.example.mychat.view.LoginScreen
import com.example.mychat.view.MessageScreen
import com.example.mychat.view.OTPScreen
import com.example.mychat.view.PasswordScreen
import com.example.mychat.view.ProfileScreen
import com.example.mychat.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    val viewModel : LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isLoading by viewModel.isLoading.collectAsState()
            val controller = rememberNavController()
            Box(Modifier.fillMaxSize()) {
                NavHost(controller, startDestination = if(viewModel.firebaseAuth.currentUser?.uid !=null)"chat" else "email") {
                    composable("email") {
                        EmailScreen(viewModel, controller)
                    }
                    composable("otp") {
                        OTPScreen(viewModel, controller)
                    }
                    composable("password") {
                        PasswordScreen(viewModel, controller)
                    }
                    composable("profile") {
                        ProfileScreen(viewModel,controller)
                    }
                    composable("chat") {
                        ChatScreen(viewModel, controller)
                    }
                    composable("login") {
                        LoginScreen(viewModel,controller)
                    }
                    composable("message") {
                        MessageScreen(viewModel)
                    }
                }
            }
            if(isLoading) {
                Box(Modifier.fillMaxSize().background(Color.Black.copy(0.3f)), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val uuid = viewModel.firebaseAuth.currentUser?.uid ?: return
        val update = hashMapOf<String, Any>(
            "isOnline" to true,
            "lastSeen" to 0L

        )
        viewModel.firebaseFireStore.collection("users").document(uuid)
            .update(update)
    }

    override fun onStop() {
        super.onStop()
        val uuid = viewModel.firebaseAuth.currentUser?.uid ?: return
        val update = hashMapOf<String, Any>(
            "isOnline" to false,
            "lastSeen" to System.currentTimeMillis()
        )
        viewModel.firebaseFireStore.collection("users").document(uuid)
            .update(update)
    }
}

