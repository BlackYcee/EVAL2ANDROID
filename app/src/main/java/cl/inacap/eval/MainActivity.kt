package cl.inacap.eval

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cl.inacap.eval.nav.AppNavGraph
import cl.inacap.eval.screens.login.AuthState
import cl.inacap.eval.screens.login.AuthViewModel
import cl.inacap.eval.ui.theme.EvalTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {


            val authViewModel: AuthViewModel = viewModel()

            val authState by authViewModel.authState.collectAsState()

            splash.setKeepOnScreenCondition { authState is AuthState.Checking }

            EvalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (authState !is AuthState.Checking) {
                            AppNavGraph(vm = authViewModel)
                        }
                    }
                }
            }
        }
    }
}