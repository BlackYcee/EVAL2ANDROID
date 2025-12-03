package cl.inacap.eval.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.inacap.eval.nav.Route
import cl.inacap.eval.screens.login.AuthViewModel

@Composable
fun ResetPasswordScreen(
    nav: NavController,
    email: String?,
    code: String?,
    vm: AuthViewModel = viewModel()
) {
    var inputCode by remember { mutableStateOf(code ?: "") }   // editable
    var backendCode by remember { mutableStateOf(code ?: "") } // último código válido
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var infoMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Cambiar contraseña", fontSize = 22.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(16.dp))

        // Email (solo lectura)
        OutlinedTextField(
            value = email ?: "",
            onValueChange = {},
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )
        Spacer(Modifier.height(8.dp))

        // Código (editable)
        OutlinedTextField(
            value = inputCode,
            onValueChange = { inputCode = it },
            label = { Text("Código") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))

        // Nueva contraseña
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("Nueva contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(8.dp))

        // Confirmar contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(16.dp))

        // Botón principal: cambiar contraseña
        Button(
            onClick = {
                when {
                    inputCode.isBlank() -> errorMessage = "Código vacío."
                    !inputCode.all { it.isDigit() } -> errorMessage = "Código debe contener solo números."
                    inputCode != backendCode -> errorMessage = "Código incorrecto."
                    newPassword.isBlank() || confirmPassword.isBlank() -> errorMessage = "Completa todos los campos."
                    newPassword != confirmPassword -> errorMessage = "Contraseñas no coinciden."
                    !isPasswordStrong(newPassword) -> errorMessage = "Contraseña débil."
                    else -> {
                        isLoading = true
                        errorMessage = null
                        vm.resetPassword(
                            email!!, inputCode, newPassword,
                            onSuccess = {
                                isLoading = false
                                Toast.makeText(nav.context, "Contraseña cambiada correctamente", Toast.LENGTH_LONG).show()
                                nav.navigate(Route.Login.path) {
                                    popUpTo(Route.ResetPassword.path) { inclusive = true }
                                }
                            },
                            onError = { msg ->
                                isLoading = false
                                errorMessage = msg
                            }
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(if (isLoading) "Cambiando..." else "Cambiar contraseña")
        }

        Spacer(Modifier.height(12.dp))

        // Botón para reenviar código
        OutlinedButton(
            onClick = {
                vm.forgotPassword(
                    email ?: "",
                    onSuccess = { newCode ->
                        backendCode = newCode   // ← guarda el nuevo código válido
                        inputCode = newCode     // opcional: lo rellena en el campo
                        infoMessage = "Nuevo código enviado: $newCode"
                    },
                    onError = { msg -> errorMessage = msg }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reenviar código")
        }

        errorMessage?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        infoMessage?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = Color(0xFF2196F3))
        }
    }
}

// Validación de robustez de contraseña
fun isPasswordStrong(password: String): Boolean {
    val lengthOk = password.length >= 8
    val hasUpper = password.any { it.isUpperCase() }
    val hasLower = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecial = password.any { !it.isLetterOrDigit() }
    return lengthOk && hasUpper && hasLower && hasDigit && hasSpecial
}
