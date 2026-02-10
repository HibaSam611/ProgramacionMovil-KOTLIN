package com.example.login.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.login.R

@Composable
fun Home(
    onGoToLogin: () -> Unit,   // Evento 1
    onGoToSignup: () -> Unit   // Evento 2
) {

    Column(
        modifier = Modifier.fillMaxSize().background(Color.Black).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(150.dp))

        Image(painter = painterResource(id = R.drawable.spotify),
            contentDescription = "",
            modifier = Modifier.width(160.dp).height(80.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Millions of songs.\nFree on Spotify.",
            color = Color.White,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(40.dp))

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {


            Button(
                onClick = { onGoToSignup() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954))
            ) {
                Text("Sign up free", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botones visuales (Google/Facebook) sin acción por ahora
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(50.dp).border(1.dp, Color.Gray, RoundedCornerShape(50.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.google), contentDescription = "", modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(20.dp))
                    Text("Continue with Google", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(50.dp).border(1.dp, Color.Gray, RoundedCornerShape(50.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.fafacebook), contentDescription = "", modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(20.dp))
                    Text("Continue with Facebook", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón Login -> Dispara evento
            Button(
                onClick = { onGoToLogin() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text("Log in", color = Color.White)
            }
        }
    }
}