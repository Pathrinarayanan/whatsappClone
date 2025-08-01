package com.example.mychat.view

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mychat.ui.theme.ctaBlue
import com.example.mychat.ui.theme.greyBackground


@Composable
fun EmailScreen(controller: NavHostController) {
    var email by remember { mutableStateOf("") }
    var buttonEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(email) {
        buttonEnabled = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    Column(
        Modifier.fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Box(
            Modifier.size(100.dp)
                .background(ctaBlue, shape = RoundedCornerShape(25.dp)),
            contentAlignment = Alignment.Center
        ){
            Text("💬",Modifier, fontSize = 50.sp, textAlign = TextAlign.Center)
        }
        Text("Welcome to Whatsapp", Modifier.padding(top = 30.dp, bottom = 10.dp),
            color = Color.Black,
            fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center,
            )

        Text("Enter you email to continue", Modifier.padding( bottom = 32.dp),
            color = Color.Gray, textAlign = TextAlign.Center,
        )
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            Modifier.fillMaxWidth().padding(horizontal = 20.dp).
            background(greyBackground, shape = RoundedCornerShape(10.dp)), placeholder = {
                Text("Enter your email Address", Modifier, color = Color.Gray, fontSize = 16.sp)
            },
            singleLine = true,
        )
        Button(
            onClick = {
                controller.navigate("otp")
            },
            Modifier.fillMaxWidth().padding(top = 30.dp).height(50.dp)
                .padding(horizontal = 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if(buttonEnabled ) ctaBlue else greyBackground
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Send OTP" , Modifier, fontWeight = FontWeight.SemiBold, color = if(buttonEnabled) Color.White
            else Color.Black)
        }
    }
}