package com.example.mychat.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mychat.ApiService
import com.example.mychat.RetrofitService
import com.example.mychat.modal.OtpRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application): AndroidViewModel(application) {

    val api = RetrofitService.getInstance().create<ApiService>(ApiService::class.java)
    val email = mutableStateOf("")
    private val _isLoading = MutableStateFlow(false)
    val isLoading  : StateFlow<Boolean> = _isLoading
    val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseFireStore : FirebaseFirestore = FirebaseFirestore.getInstance()

    fun sendOtp(controller: NavHostController) {
        _isLoading.value = true
        val context = getApplication<Application>().applicationContext
        viewModelScope.launch {
            val response = api.sendOtp(OtpRequest(email.value, ""))
            try {
                if (response.success == true) {
                    controller.navigate("otp")
                    Toast.makeText(context, "OTP- Sent", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                }
            }
            catch (
                e: Exception
            ){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()

            }
            finally {
                _isLoading.value = false
            }
        }
    }
    fun login(password: String, controller: NavHostController){
        val context = getApplication<Application>().applicationContext
        firebaseAuth.signInWithEmailAndPassword(email.value, password)
            .addOnSuccessListener {
                Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show()
                controller.navigate("chat")
            }
            .addOnFailureListener {
                Toast.makeText(context, "Login Failed, ${it.message}", Toast.LENGTH_LONG).show()

            }
    }

    fun verifyOtp(otp: String, controller: NavHostController){
        val context = getApplication<Application>().applicationContext
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = api.verifyOtp(OtpRequest(email.value, otp))
                if (response.success == true) {
                    Toast.makeText(context, "OTP verified", Toast.LENGTH_LONG).show()
                    checkEmailExists(email.value){ exists->
                        if(exists){
                            // go to login page
                            controller.navigate("login")
                        }
                        else{
                            controller.navigate("password")
                        }
                    }
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                }
            }catch (e : Exception){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()

            }
            finally {
                _isLoading.value = false
            }
        }
    }
    fun registerUser(password: String, controller: NavHostController){
        _isLoading.value = true
        val context = getApplication<Application>().applicationContext
        try {
            firebaseAuth.createUserWithEmailAndPassword(email.value, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "User Registered", Toast.LENGTH_LONG).show()
                        val user = firebaseAuth.currentUser
                        val userData = hashMapOf(
                            "uid" to user?.uid,
                            "email" to user?.email,
                            "name" to "",
                            "createdAt" to System.currentTimeMillis()
                        )
                        firebaseFireStore.collection("users").document(user!!.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                controller.navigate("profile")
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, it.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                    } else {
                        Toast.makeText(context, "error in user registration", Toast.LENGTH_LONG)
                            .show()

                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
        }catch (e : Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
        finally {
            _isLoading.value = false
        }
    }

    fun checkEmailExists(email :String , onResult : (Boolean)->Unit){
        firebaseFireStore.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener {
                val exists = !it.isEmpty
                onResult(exists)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }


    fun uriToBase64(context : Context, uri : Uri): String {
        val inputStream =context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes() ?: return ""
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
    fun uploadImage(name : String, url : String , controller: NavController){
        val context = getApplication<Application>().applicationContext
        val uuid = firebaseAuth.currentUser?.uid ?:return

        val base64Image = uriToBase64(context, url.toUri())
        if(base64Image.length>9000_000){
            Toast.makeText(context, "Image too large, choose smaller one", Toast.LENGTH_LONG)
                .show()
            return
        }
        val userMap = hashMapOf(
            "uid" to uuid,
            "email" to email.value,
            "name" to name,
            "profileImage" to base64Image,
            "createdAt" to System.currentTimeMillis()
        )
        firebaseFireStore.collection("users").document(uuid)
            .set(userMap)
            .addOnSuccessListener {
                controller.navigate("chat")
            }
            .addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG)
                    .show()
                    }
    }
}