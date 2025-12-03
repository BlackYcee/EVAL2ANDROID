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
            .padding(16.dp) // menos padding general
    ) {
        Text("Lista de Usuarios", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))

        users.forEach { user ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(user.name, style = MaterialTheme.typography.titleMedium)
                    Text(user.email, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { nav.navigate("editarUsuario/${user.id}") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = burdeo,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 8.dp) // compacto
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
                            ),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { nav.navigate(Route.CrudUsuario.path) },
            colors = ButtonDefaults.buttonColors(
                containerColor = burdeo,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            Text("Volver")
        }
    }
}
