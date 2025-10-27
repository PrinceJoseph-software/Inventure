package com.example.inventure

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthScreen(onLoginSuccess: () -> Unit) {
    var isSignUp by remember { mutableStateOf(false) }

    val bgColor by animateColorAsState(
        targetValue = if (isSignUp) Color(0xFF4B0082) else Color.White,
        animationSpec = tween(600)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = isSignUp,
            transitionSpec = {
                slideInHorizontally(
                    animationSpec = tween(600),
                    initialOffsetX = { if (targetState) it else -it }
                ) togetherWith slideOutHorizontally(
                    animationSpec = tween(600),
                    targetOffsetX = { if (targetState) -it else it }
                )
            },
            label = "auth_transition"
        ) { target ->
            if (target) {
                SignUpPanel(onSwitchToLogin = { isSignUp = false })
            } else {
                SignInPanel(onSwitchToSignUp = { isSignUp = true }, onLoginSuccess)
            }
        }
    }
}

@Composable
fun SignInPanel(onSwitchToSignUp: () -> Unit, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sign In", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4B0082),
                    unfocusedBorderColor = Color.Gray
                )
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4B0082),
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onLoginSuccess,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B0082))
            ) {
                Text("Sign In")
            }

            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onSwitchToSignUp) {
                Text("Don't have an account? Sign Up", color = Color(0xFF4B0082))
            }
        }
    }
}

@Composable
fun SignUpPanel(onSwitchToLogin: () -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sign Up", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4B0082))

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4B0082),
                    unfocusedBorderColor = Color.Gray
                )
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4B0082),
                    unfocusedBorderColor = Color.Gray
                )
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4B0082),
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onSwitchToLogin,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4B0082),
                    contentColor = Color.White
                )
            ) {
                Text("Sign Up")
            }

            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onSwitchToLogin) {
                Text("Already have an account? Sign In", color = Color(0xFF4B0082))
            }
        }
    }
}