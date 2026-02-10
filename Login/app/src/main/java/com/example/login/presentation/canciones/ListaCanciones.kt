package com.example.login.presentation.canciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ListaCanciones(auth: FirebaseAuth, navController: NavController){

    Column(modifier = Modifier
        .background(Color.Black)
            .fillMaxSize()
        .padding(0.dp, 150.dp)
        ) {
        Text("Canción 1", color =  Color.White)
        Text("Canción 2", color = Color.White)
        Text("Canción 3", color = Color.White)
        Text("Canción 4", color = Color.White)
        Text("Canción 5", color = Color.White)
        Text("Canción 6", color = Color.White)
        Text("Canción 7", color = Color.White)
        Text("Canción 8", color = Color.White)

    }
}