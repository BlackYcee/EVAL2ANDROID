package cl.inacap.eval.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.inacap.eval.nav.Route

@Composable
fun ListarUsuariosScreen(nav: NavController, vm: UserViewModel = viewModel()) {
    // Cargar usuarios al entrar
    LaunchedEffect(Unit) {
        vm.loadUsers()
    }

    val users by vm.users.collectAsState(initial = emptyList())
    val burdeo = Color(0xFF800020)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Lista de Usuarios", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        users.forEach { user ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${user.name} - ${user.email}")

                Row {
                    Button(
                        onClick = { nav.navigate("editarUsuario/${user.id}") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = burdeo,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Editar")
                    }
                    Button(
                        onClick = {
                            user.id?.let { id ->
                                vm.deleteUser(id)
                                vm.loadUsers()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Eliminar")
                    }

                }
            }
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

