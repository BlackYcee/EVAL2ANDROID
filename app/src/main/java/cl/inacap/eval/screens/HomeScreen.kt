package cl.inacap.eval.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.inacap.eval.data.remote.dto.UserDto
import cl.inacap.eval.nav.Route
import cl.inacap.eval.screens.login.AuthState
import cl.inacap.eval.screens.login.AuthViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.testing.TestNavHostController
import cl.inacap.eval.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.delay


@Composable
fun LoginContent(
    authState: AuthState,
    nav: NavController,
    onLogout: () -> Unit
) {
    val burdeo = Color(0xFF800020)

    // Estado para la fecha y hora
    var currentDateTime by remember { mutableStateOf("") }

    // Actualizar cada segundo
    LaunchedEffect(Unit) {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        while (true) {
            val now = Date()
            currentDateTime = formatter.format(now)
            delay(1000) // refresca cada segundo
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.si),
            contentDescription = "Logo de la app",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 12.dp)
                .align(Alignment.CenterHorizontally)
        )
        // Mostrar fecha y hora arriba
        Text(
            text = currentDateTime,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(24.dp))

        if (authState is AuthState.Authenticated) {
            val user = authState.user
            Text("Bienvenido ${user.name}", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(24.dp))
        }

        // Botón CRUD Usuario
        Button(
            onClick = { nav.navigate(Route.CrudUsuario.path) },
            colors = ButtonDefaults.buttonColors(
                containerColor = burdeo,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("CRUD Usuario")
        }

        // Botón Datos del Sensor
        Button(
            onClick = { nav.navigate(Route.Iot.path) },
            colors = ButtonDefaults.buttonColors(
                containerColor = burdeo,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Datos del Sensor")
        }

        // Botón Desarrollador
        Button(
            onClick = { nav.navigate(Route.Desarrollador.path) },
            colors = ButtonDefaults.buttonColors(
                containerColor = burdeo,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Desarrollador")
        }

        Spacer(Modifier.height(24.dp))

        // Botón Cerrar sesión
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}


@Composable
fun HomeScreen(
    nav: NavController,
    vm: AuthViewModel = viewModel(),
    onLogoutDone: () -> Unit
) {
    val authState by vm.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            onLogoutDone()
        }
    }

    LoginContent(
        authState = authState,
        nav = nav,
        onLogout = { vm.logout() }
    )
}

// --- Preview Components ---
@Composable
fun HomeContentPreviewUI(
    authState: AuthState,
    nav: NavHostController,
    onLogout: () -> Unit = {}
) {
    AppIotComposeTheme {
        LoginContent(
            email = "",
            pass = "",
            authState = authState,
            onEmailChange = {},
            onPassChange = {},
            onLoginClick = {},
            onRegisterClick = { nav.navigate(Route.Register.path) },
            onForgotPasswordClick = { nav.navigate(Route.ForgotPassword.path) }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val fakeNav = TestNavHostController(LocalContext.current)

    HomeContentPreviewUI(
        authState = AuthState.Authenticated(
            user = UserDto(
                id = 1,
                name = "nadie",
                last_name = "nada",
                email = "nada@example.com"
            )
        ),
        nav = fakeNav
    )
}
