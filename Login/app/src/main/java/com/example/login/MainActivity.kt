package com.example.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.login.presentation.canciones.ListaCanciones
import com.example.login.presentation.home.Home
import com.example.login.presentation.login.Login
import com.example.login.presentation.signup.SignUp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "home") {

                // PANTALLA HOME
                composable("home") {
                    Home(
                        onGoToLogin = { navController.navigate("login") },
                        onGoToSignup = { navController.navigate("signup") }
                    )
                }

                // PANTALLA LOGIN
                composable("login") {
                    Login(
                        auth = auth,
                        onLoginSuccess = {
                            navController.navigate("canciones") {
                            }
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                // PANTALLA SIGNUP
                composable("signup") {
                    SignUp(
                        auth = auth,
                        onSignupSuccess = {
                            navController.navigate("canciones") {
                                popUpTo("signup") { inclusive = true }
                            }
                        },
                        onBack = { navController.popBackStack() } , // Volver atrás
                        navController = navController
                    )
                }

                composable("canciones") {
                    ListaCanciones(auth, navController)
                }

            }
        }
    }
    override fun onStart()
    {
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null)
        {
            // navegar a otra pagina
        }
    }
}
