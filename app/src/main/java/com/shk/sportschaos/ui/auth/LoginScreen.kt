package com.shk.sportschaos.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// ══════════════════════════════════════════════════════════════
// LOGIN SCREEN COMPOSABLE
//
// @Composable = This function creates UI
// Parameters:
// - viewModel: Handles logic and state
// - onLoginSuccess: Called when login succeeds
// - onNavigateToRegister: Called when user taps "Register"
// ══════════════════════════════════════════════════════════════
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    // ════════════════════════════════════════════════════════
    // STATE VARIABLES
    // remember = Survives recomposition (UI redraw)
    // mutableStateOf = Can be changed, triggers UI update
    // ════════════════════════════════════════════════════════
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // ════════════════════════════════════════════════════════
    // OBSERVE UI STATE
    // collectAsState() converts Flow to Compose State
    // UI automatically updates when state changes
    // ════════════════════════════════════════════════════════
    val uiState by viewModel.uiState.collectAsState()

    // ════════════════════════════════════════════════════════
    // SIDE EFFECT - Handle login success
    // LaunchedEffect runs when key (uiState) changes
    // ════════════════════════════════════════════════════════
    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onLoginSuccess()
        }
    }

    // ════════════════════════════════════════════════════════
    // UI LAYOUT
    // Column = Vertical stack of components
    // Modifier = Styling (padding, size, alignment, etc.)
    // ════════════════════════════════════════════════════════
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ═══ APP TITLE ═══
        Text(
            text = "Sports Chaos",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(48.dp))

        // ═══ EMAIL INPUT ═══
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is LoginUiState.Loading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ═══ PASSWORD INPUT ═══
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible)
                            "Hide password"
                        else
                            "Show password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is LoginUiState.Loading
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ═══ LOGIN BUTTON ═══
        Button(
            onClick = {
                viewModel.login(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = uiState !is LoginUiState.Loading
        ) {
            if (uiState is LoginUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Login", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ═══ REGISTER LINK ═══
        TextButton(onClick = onNavigateToRegister) {
            Text("Don't have an account? Register")
        }

        // ═══ ERROR MESSAGE ═══
        if (uiState is LoginUiState.Error) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = (uiState as LoginUiState.Error).message,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}