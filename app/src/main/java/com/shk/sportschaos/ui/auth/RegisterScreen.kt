package com.shk.sportschaos.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
// REGISTER SCREEN
// ══════════════════════════════════════════════════════════════
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    // ════════════════════════════════════════════════════════
    // STATE VARIABLES
    // ════════════════════════════════════════════════════════
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    // ════════════════════════════════════════════════════════
    // HANDLE SUCCESS
    // ════════════════════════════════════════════════════════
    LaunchedEffect(uiState) {
        if (uiState is RegisterUiState.Success) {
            onRegisterSuccess()
        }
    }

    // ════════════════════════════════════════════════════════
    // UI LAYOUT - Added scroll for smaller screens
    // ════════════════════════════════════════════════════════
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ═══ TITLE ═══
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Join Sports Chaos",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ═══ NAME INPUT ═══
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is RegisterUiState.Loading
        )

        Spacer(modifier = Modifier.height(16.dp))

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
            enabled = uiState !is RegisterUiState.Loading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ═══ PHONE NUMBER INPUT (Optional) ═══
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number (Optional)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is RegisterUiState.Loading
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
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible)
                            "Hide password"
                        else
                            "Show password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is RegisterUiState.Loading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ═══ CONFIRM PASSWORD INPUT ═══
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = if (confirmPasswordVisible)
                            "Hide password"
                        else
                            "Show password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is RegisterUiState.Loading
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ═══ REGISTER BUTTON ═══
        Button(
            onClick = {
                viewModel.register(
                    name = name,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    phoneNumber = phoneNumber.ifBlank { null }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = uiState !is RegisterUiState.Loading
        ) {
            if (uiState is RegisterUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Register", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ═══ LOGIN LINK ═══
        TextButton(onClick = onNavigateToLogin) {
            Text("Already have an account? Login")
        }

        // ═══ ERROR MESSAGE ═══
        if (uiState is RegisterUiState.Error) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = (uiState as RegisterUiState.Error).message,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}