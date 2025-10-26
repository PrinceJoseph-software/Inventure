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
            Text("Sign In", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Email") })
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Password") })

            Spacer(Modifier.height(16.dp))
            Button(onClick = onLoginSuccess) { Text("Sign In") }

            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onSwitchToSignUp) { Text("Don’t have an account? Sign Up") }
        }
    }
}

@Composable
fun SignUpPanel(onSwitchToLogin: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4B0082)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sign Up", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Full Name") })
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Email") })
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Password") })

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onSwitchToLogin,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF4B0082))
            ) {
                Text("Sign Up")
            }

            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onSwitchToLogin) {
                Text("Already have an account? Sign In", color = Color.White)
            }
        }
    }
}
