package cl.inacap.eval.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.inacap.eval.nav.Route
import cl.inacap.eval.data.AuthRepository
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(nav: NavController) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }
    var confirmPwd by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    val repo = AuthRepository()
    val scope = rememberCoroutineScope()

    val burdeo = Color(0xFF800020)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = pwd,
            onValueChange = { pwd = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPwd,
            onValueChange = { confirmPwd = it },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                scope.launch {
                    // Validación de campos vacíos
                    if (name.isBlank() || lastName.isBlank() || email.isBlank() || pwd.isBlank() || confirmPwd.isBlank()) {
                        message = "Campos obligatorios vacíos"
                        isError = true
                        return@launch
                    }

                    // Validación formato de email
                    val emailRegex = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
                    if (!emailRegex.matches(email)) {
                        message = "Formato de correo inválido."
                        isError = true
                        return@launch
                    }

                    // Validación contraseña robusta (mínimo 8 caracteres, mayúscula, minúscula, número y símbolo)
                    val pwdRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
                    if (!pwdRegex.matches(pwd)) {
                        message = "Contraseña débil. Debe tener al menos 8 caracteres, mayúscula, minúscula, número y símbolo."
                        isError = true
                        return@launch
                    }

                    // Confirmación de contraseña
                    if (pwd != confirmPwd) {
                        message = "Las contraseñas no coinciden."
                        isError = true
                        return@launch
                    }

                    // Validación email repetido contra BD
                    val exists = repo.checkEmailExists(email)
                    if (exists) {
                        message = "Email ya registrado."
                        isError = true
                        return@launch
                    }

                    // Intento de registro
                    val result = repo.register(name, lastName, email, pwd)
                    result.onSuccess {
                        message = "Registro exitoso."
                        isError = false
                        nav.navigate(Route.Login.path) // redirige a Login, no inicia sesión
                    }.onFailure {
                        message = "Error de servidor / error al guardar."
                        isError = true
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = burdeo,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear cuenta", style = MaterialTheme.typography.bodyLarge)
        }

        message?.let {
            Spacer(Modifier.height(16.dp))
            Text(
                text = it,
                color = if (isError) Color.Red else Color(0xFF2196F3),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "¿Ya tienes cuenta? Inicia sesión",
            color = Color.Blue,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable { nav.navigate(Route.Login.path) }
        )
    }
}

