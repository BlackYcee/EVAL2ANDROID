package cl.inacap.eval.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.inacap.eval.data.remote.dto.UserDto
import cl.inacap.eval.nav.Route

@Composable
fun EditarUsuarioScreen(
    nav: NavController,
    userId: Int,
    vm: UserViewModel = viewModel()
) {
    // Cargar usuarios al entrar
    LaunchedEffect(Unit) {
        vm.loadUsers()
    }

    val users by vm.users.collectAsState()
    val user = users.find { it.id == userId }

    var name by remember { mutableStateOf(user?.name ?: "") }
    var last_name by remember {mutableStateOf(user?.last_name ?: "")}
    var email by remember { mutableStateOf(user?.email ?: "") }

    val burdeo = Color(0xFF800020)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Editar Usuario", style = MaterialTheme.typography.headlineSmall)
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
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val updatedUser = UserDto(
                    id = userId,
                    name = name,
                    last_name = last_name,
                    email = email
                )
                vm.updateUser(userId, updatedUser)
                nav.popBackStack() // volver a la lista
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = burdeo,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar cambios")
        }

        Button (
            onClick= {nav.navigate(Route.CrudUsuario.path)},
            colors = ButtonDefaults.buttonColors(
                containerColor = burdeo,
                contentColor = Color.White
            ),
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text("Volver")
        }
    }
}
