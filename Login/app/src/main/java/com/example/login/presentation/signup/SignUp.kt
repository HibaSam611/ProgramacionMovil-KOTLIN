package com.example.login.presentation.signup

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.example.login.R

@Composable
fun SignUp(
    auth: FirebaseAuth,
    onBack: () -> Unit
    , navController: NavController, onSignupSuccess: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember {  mutableStateOf("")}
    var mensajeError by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(Color.Black).padding(24.dp)) {

        Image(painter = painterResource(id = R.drawable.back),
            contentDescription = "Volver",
            modifier = Modifier.size(20.dp).clickable { onBack() }
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text("What's your email?", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email, onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(focusedContainerColor = Color.DarkGray, unfocusedContainerColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text("You'll need to confirm this email later.", color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            value = password, onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(focusedContainerColor = Color.DarkGray, unfocusedContainerColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
        )
        Spacer(modifier = Modifier.height(30.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = {
                    if (esValidaContraseña(password)) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user: String = auth.currentUser?.email!!
                                    Log.println(Log.INFO, "login info", user)
                                    onSignupSuccess()

                                } else {
                                    mensajeError = "ERROR: usuario o contraseña incorrectos"
                                    Log.println(Log.INFO, "login info", "errooor")
                                }
                            }
                    }else{
                        Toast.makeText(context, "Contraseña mal", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = email.isNotEmpty(),
                modifier = Modifier.width(100.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1DB954),
                    contentColor = Color.Black,
                    disabledContainerColor = Color.DarkGray,
                    disabledContentColor = Color.Black
                ),
                shape = RoundedCornerShape(50)
            ) {
                Text("Next")
            }
        }
    }
}
fun esValidaContraseña(password: String): Boolean{
    //al menos 8 caracteres
    if (password.length < 8){ return false}
//    if (!password.contains("$") && !password.contains("?") && !password.contains(".") && !password.contains("*")){
//        return false
//    }
    if(!Regex("[\$?.*]").containsMatchIn(password)){return false} //al menos un caracter especial
    if(!Regex("[A-Z]+").containsMatchIn(password)){return false} // al menos una mayuscula

    return true
}