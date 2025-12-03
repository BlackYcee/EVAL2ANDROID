package cl.inacap.eval.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.inacap.eval.nav.Route
import cl.inacap.eval.screens.login.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    nav: NavController,
    vm: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Recuperar contraseña",
            fontSize = 22.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isNotBlank()) {
                    isLoading = true
                    errorMessage = null
                    vm.forgotPassword(
                        email,
                        onSuccess = { code ->
                            isLoading = false
                            // me voy a volver loco, ni idea como funciona esto pero funciona
                            nav.navigate(Route.ResetPassword.path + "?email=$email&code=$code")

                        },
                        onError = { msg ->
                            isLoading = false
                            errorMessage = msg
                        }
                    )
                } else {
                    errorMessage = "Ingresa tu email"
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) "Enviando..." else "Enviar código")
        }

        if (errorMessage != null) {
            Spacer(Modifier.height(8.dp))
            Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
        }
    }
}
