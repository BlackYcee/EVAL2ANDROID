package cl.inacap.eval.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.inacap.eval.data.remote.dto.CreateUserRequest
import cl.inacap.eval.nav.Route

@Composable
fun IngresarUsuarioScreen(nav: NavController, vm: UserViewModel = viewModel()) {
    var name by remember { mutableStateOf("") }
    var last_name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }

    val burdeo = Color(0xFF800020)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Ingresar Usuario", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = last_name,
            onValueChange = { last_name = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = pwd,
            onValueChange = { pwd = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                // Validaciones
                if (name.isBlank() || last_name.isBlank() || email.isBlank() || pwd.isBlank()) {
                    message = "Campos obligatorios vacíos"
                    isError = true
                    return@Button
                }

                val emailRegex = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
                if (!emailRegex.matches(email)) {
                    message = "Formato de correo inválido"
                    isError = true
                    return@Button
                }

                val pwdRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
                if (!pwdRegex.matches(pwd)) {
                    message = "Contraseña débil. Debe tener al menos 8 caracteres, mayúscula, minúscula, número y símbolo."
                    isError = true
                    return@Button
                }

                // Si todo está OK, crear usuario
                val newUser = CreateUserRequest(
                    name = name,
                    email = email,
                    last_name = last_name,
                    password_hash = pwd
                )
                vm.insertUser(newUser)
                message = "Usuario ingresado correctamente"
                isError = false
                nav.popBackStack() // volver a la lista
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = burdeo,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { nav.navigate(Route.CrudUsuario.path) },
            colors = ButtonDefaults.buttonColors(
                containerColor = burdeo,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }

        message?.let {
            Spacer(Modifier.height(16.dp))
            Text(
                it,
                color = if (isError) Color.Red else Color.Green,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
