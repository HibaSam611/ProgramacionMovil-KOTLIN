package com.example.login.presentation.login

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.example.login.R

@Composable
fun Login(
    auth: FirebaseAuth,
    onLoginSuccess: () -> Unit, // Evento ir a home
    onBack: () -> Unit          // Evento volver atrás
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val botonHabilitado = email.isNotEmpty() && password.isNotEmpty()
    var mensajeError by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black).padding(24.dp)) {

        Image(painter = painterResource(id = R.drawable.back),
            contentDescription = "Volver",
            modifier = Modifier.size(20.dp).clickable { onBack() }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text("Email or username", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email, onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(focusedContainerColor = Color.DarkGray, unfocusedContainerColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text("Password", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password, onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(focusedContainerColor = Color.DarkGray, unfocusedContainerColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
        )
        Text(mensajeError, color = Color.Red)


        Spacer(modifier = Modifier.height(30.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user: String = auth.currentUser?.email!!
                            Log.println(Log.INFO, "login info", user)
                            onLoginSuccess()
                        }else{
                            mensajeError = "ERROR: usuario o contraseña incorrectos"
                        }
                    }
                },
                enabled = botonHabilitado,
                modifier = Modifier.width(140.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954), contentColor = Color.Black, disabledContainerColor = Color.DarkGray, disabledContentColor = Color.Black),
                shape = RoundedCornerShape(50)
            ) {
                Text("Log in")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White),
                shape = RoundedCornerShape(50), border = BorderStroke(1.dp, Color.Gray)
            ) {
                Text("Log in without password")
            }
        }
    }
}