package cl.inacap.eval.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.inacap.eval.R
import cl.inacap.eval.nav.Route

@Composable
fun CrudMenuScreen(nav: NavController) {
    val burdeo = Color(0xFF800020)

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
        Button(
            onClick = { nav.navigate(Route.IngresarUsuario.path) },
            colors = ButtonDefaults.buttonColors(
                containerColor = burdeo,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Ingresar Usuario")
        }

        Button(
            onClick = { nav.navigate(Route.ListarUsuarios.path) },
            colors = ButtonDefaults.buttonColors(
                containerColor = burdeo,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Listar Usuarios")
        }
        Button(
            onClick = { nav.navigate(Route.Home.path) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver a Home")
        }
    }
}
