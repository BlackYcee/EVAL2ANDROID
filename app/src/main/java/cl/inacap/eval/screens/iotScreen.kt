// Paquete base de tu pantalla
package cl.inacap.eval.screens

// Layouts y modificadores
import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

// Material Design 3
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import cl.inacap.eval.R

// Estados y corrutinas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment

// Colores y estilos
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Navegación
import androidx.navigation.NavController
import cl.inacap.eval.data.IotRepository

// Tu repositorio y DTO de IoT
import cl.inacap.eval.data.remote.dto.IotDataResponse
import kotlinx.coroutines.delay

// Corrutinas
import kotlinx.coroutines.launch


@Composable
fun IotScreen(nav: NavController, repo: IotRepository = IotRepository()) {
    var data by remember { mutableStateOf<IotDataResponse?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var bulbOn by remember { mutableStateOf(false) }
    var flashlightOn by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Intervalo configurable (ej: 2 segundos)
    LaunchedEffect(Unit) {
        while (true) {
            scope.launch {
                val result = repo.getData()
                result.onSuccess { response ->
                    data = response
                    error = null
                }.onFailure {
                    error = it.message
                }
            }
            delay(2000) // cada 2 segundos
        }
    }

    Column(
        Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Datos IoT", fontSize = 22.sp)
        Spacer(Modifier.height(16.dp))

        when {
            error != null -> Text("Error: $error", color = Color.Red)
            data != null -> {
                // Icono dinámico de temperatura
                val tempIcon = if (data!!.temperature > 20) R.drawable.ic_temp_high else R.drawable.ic_temp_low
                Image(
                    painter = painterResource(id = tempIcon),
                    contentDescription = "Temperatura",
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text("Temperatura: ${data!!.temperature} °C")
                Text("Humedad: ${data!!.humidity} %")
                Text("Hora: ${data!!.timestamp}")
            }
            else -> Text("Cargando datos...")
        }

        Spacer(Modifier.height(24.dp))

        // Ampolleta
        val bulbIcon = if (bulbOn) R.drawable.ic_bulb_on else R.drawable.ic_bulb_off
        Image(
            painter = painterResource(id = bulbIcon),
            contentDescription = "Ampolleta",
            modifier = Modifier
                .size(80.dp)
                .clickable { bulbOn = !bulbOn }
        )
        Text(if (bulbOn) "Ampolleta encendida" else "Ampolleta apagada")

        Spacer(Modifier.height(24.dp))

        // Linterna
        val flashlightIcon = if (flashlightOn) R.drawable.ic_flash_on else R.drawable.ic_flash_off
        Image(
            painter = painterResource(id = flashlightIcon),
            contentDescription = "Linterna",
            modifier = Modifier
                .size(80.dp)
                .clickable {
                    flashlightOn = !flashlightOn
                    toggleFlashlight(context, flashlightOn)
                }
        )
        Text(if (flashlightOn) "Linterna activada" else "Linterna desactivada")

        Spacer(Modifier.height(24.dp))

        Button(onClick = { nav.popBackStack() }) {
            Text("Volver")
        }
    }
}

// Función para encender/apagar linterna
fun toggleFlashlight(context: Context, turnOn: Boolean) {
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val cameraId = cameraManager.cameraIdList.firstOrNull()
    cameraId?.let {
        cameraManager.setTorchMode(it, turnOn)
    }
}
