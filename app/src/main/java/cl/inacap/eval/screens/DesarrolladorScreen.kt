package cl.inacap.eval.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.inacap.eval.nav.Route
import cl.inacap.eval.R
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

data class Developer(
    val avatarRes: Int,
    val nombre: String,
    val apellido: String,
    val rol: String,
    val correo: String,
    val institucion: String,
    val carrera: String,
    val seccion: String,
    val github: String,
    val linkedin: String
)

@Composable
fun DesarrolladorScreen(nav: NavController) {
    val burdeo = Color(0xFF800020)

    val developers = listOf(
        Developer(
            avatarRes = R.drawable.halo,
            nombre = "Nicolás",
            apellido = "Galleguillos",
            rol = "Brigido",
            correo = "nicolas.galleguillos14@inacapmail.cl",
            institucion = "INACAP",
            carrera = "Ingeniería en Informática",
            seccion = "no tengo idea",
            github = "https://github.com/BlackYcee",
            linkedin = "https://www.linkedin.com/in/nicol%C3%A1s-giovanni-galleguillos-cortes-4928b4342"
        ),
        Developer(
            avatarRes = R.drawable.so,
            nombre = "Luciano",
            apellido = "Cortés",
            rol = "Desarrollador",
            correo = "luciano.cortes@inacapmail.cl",
            institucion = "INACAP",
            carrera = "Ingeniería en Informática",
            seccion = "nn",
            github = "https://github.com/lucianocortes",
            linkedin = "https://www.linkedin.com/in/luciano-cortes"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Datos del Desarrollador", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        // Lista scrolleable
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(developers) { dev ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = dev.avatarRes),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(160.dp) // más compacto
                                .padding(bottom = 8.dp)
                        )

                        Text("${dev.nombre} ${dev.apellido}", style = MaterialTheme.typography.titleMedium)
                        Text("Rol: ${dev.rol}")
                        Text("Correo: ${dev.correo}")
                        Text("Institución: ${dev.institucion}")
                        Text("Carrera: ${dev.carrera}")
                        Text("Sección: ${dev.seccion}")
                        Spacer(Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ClickableText(
                                text = AnnotatedString("GitHub"),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = burdeo,
                                    textDecoration = TextDecoration.Underline
                                ),
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dev.github))
                                    nav.context.startActivity(intent)
                                }
                            )
                            ClickableText(
                                text = AnnotatedString("LinkedIn"),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = burdeo,
                                    textDecoration = TextDecoration.Underline
                                ),
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dev.linkedin))
                                    nav.context.startActivity(intent)
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { nav.navigate(Route.Home.path) },
            colors = ButtonDefaults.buttonColors(
                containerColor = burdeo,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver a Home")
        }
    }
}
