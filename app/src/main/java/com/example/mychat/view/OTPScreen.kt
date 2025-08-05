package com.example.mychat.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mychat.ui.theme.ctaBlue
import com.example.mychat.ui.theme.greyBackground
import com.example.mychat.viewmodel.LoginViewModel


@Composable
fun OTPScreen(viewmodel: LoginViewModel, controller: NavHostController) {
    var otpInput by remember { mutableStateOf("") }
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
        ) {
            Text("💬", Modifier, fontSize = 50.sp, textAlign = TextAlign.Center)
        }
        Text(
            "Verification Code", Modifier.padding(top = 30.dp, bottom = 10.dp),
            color = Color.Black,
            fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center,
        )

        Text(
            "We've sent a 6-digit code to the ${viewmodel.email.value}", Modifier.padding(bottom = 32.dp),
            color = Color.Gray, textAlign = TextAlign.Center,
        )
        OutlinedTextField(value = otpInput, onValueChange = {input->
            if(input.length<=6 && input.all { it.isDigit() }){
                otpInput = input
            }
        }, Modifier, placeholder = {
            Text("Enter 6 -digit code",Modifier, color = Color.Gray)
        },
            colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent, cursorColor = ctaBlue,
                focusedContainerColor = greyBackground,
                unfocusedContainerColor = greyBackground
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done)

            )
        Button(
            onClick = {
                if(otpInput.length == 6){
                    viewmodel.verifyOtp(otpInput,controller)
                }
            },
            Modifier.padding(top = 30.dp), enabled = (otpInput.length ==6),
            colors = ButtonDefaults.buttonColors(
                containerColor = if(otpInput.length ==6) ctaBlue else greyBackground
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Verify & continue", Modifier.padding(horizontal = 50.dp), fontWeight = FontWeight.SemiBold,
                color = if(otpInput.length ==6) Color.White else Color.Black)
        }
        Spacer(Modifier.height(24.dp))
        TextButton(
            onClick = {}
        ) {
            Text("Didn't receive code? Resend", Modifier,color = ctaBlue)
        }
    }
}















