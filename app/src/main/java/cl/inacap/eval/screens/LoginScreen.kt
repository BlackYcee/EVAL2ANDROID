package cl.inacap.eval.screens

import cl.inacap.eval.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.inacap.eval.screens.login.AuthState
import cl.inacap.eval.screens.login.AuthViewModel
import cl.inacap.eval.nav.Route

@Composable
fun LoginContent(
    email: String,
    pass: String,
    authState: AuthState,
    onEmailChange: (String) -> Unit,
    onPassChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val isLoading = authState is AuthState.Checking
    val errorMessage = (authState as? AuthState.Error)?.message
    val successMessage = if (authState is AuthState.Authenticated) "Ingreso exitoso" else null

    var passwordVisible by remember { mutableStateOf(false) }
    val burdeo = Color(0xFF800020)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.si),
            contentDescription = "Logo de la app",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 12.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            "Bienvenido",
            fontSize = 23.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = pass,
            onValueChange = onPassChange,
            label = { Text("Contraseña") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isBlank() || pass.isBlank()) {

                } else if (!Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(email)) {

                } else {
                    onLoginClick()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = burdeo,
                contentColor = Color.White
            ),
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Ingresando...")
            } else {
                Text("Ingresar")
            }
        }

        // Mensajes de error o éxito
        errorMessage?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        successMessage?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = Color(0xFF4CAF50)) // verde éxito
        }

        Spacer(Modifier.height(12.dp))

        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Registrarme")
        }

        TextButton(
            onClick = onForgotPasswordClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("¿Olvidaste tu contraseña?")
        }
    }
}

@Composable
fun LoginScreen(nav: NavController, vm: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    val authState by vm.authState.collectAsState()

    // Si AuthState cambió a Authenticated → navega
    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            nav.navigate(Route.Home.path) {
                popUpTo(Route.Login.path) { inclusive = true }
            }
        }
    }

    LoginContent(
        email = email,
        pass = pass,
        authState = authState,
        onEmailChange = { email = it },
        onPassChange = { pass = it },
        onLoginClick = { vm.login(email.trim(), pass) },
        onRegisterClick = { nav.navigate(Route.Register.path) },
        onForgotPasswordClick = { nav.navigate(Route.ForgotPassword.path) }
    )
}

@Preview(showBackground = true)
@Composable
fun LoginContentPreview() {
    MaterialTheme {
        LoginContent(
            email = "javier@demo.cl",
            pass = "123456",
            authState = AuthState.Unauthenticated,
            onEmailChange = {},
            onPassChange = {},
            onLoginClick = {},
            onRegisterClick = {},
            onForgotPasswordClick = {}
        )
    }
}

@Composable
fun AppIotComposeTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}