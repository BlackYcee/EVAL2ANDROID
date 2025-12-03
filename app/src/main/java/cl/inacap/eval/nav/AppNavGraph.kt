package cl.inacap.eval.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument

// UI de Compose
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

// Lottie
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

// Recursos
import cl.inacap.eval.R

// Pantallas y clases propias
import cl.inacap.eval.screens.*
import cl.inacap.eval.screens.login.AuthState
import cl.inacap.eval.screens.login.AuthViewModel

// Corrutinas
import kotlinx.coroutines.delay

@Composable
fun AppNavGraph(vm: AuthViewModel = viewModel()) {
    val nav = rememberNavController()
    val authState by vm.authState.collectAsState()

    NavHost(navController = nav, startDestination = "splash") {

        composable("splash") {
            SplashLottie()

            LaunchedEffect(authState) {
                val MINIMUM_SPLASH_TIME = 1500L
                val sessionCheckStartTime = System.currentTimeMillis()

                if (authState !is AuthState.Checking) {
                    val timeElapsed = System.currentTimeMillis() - sessionCheckStartTime
                    val remainingTime = MINIMUM_SPLASH_TIME - timeElapsed

                    if (remainingTime > 0) {
                        delay(remainingTime)
                    }

                    when (authState) {
                        is AuthState.Authenticated -> {
                            nav.navigate(Route.Home.path) {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                        AuthState.Unauthenticated,
                        is AuthState.Error -> {
                            nav.navigate(Route.Login.path) {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                        else -> { /* No-op */ }
                    }
                }
            }
        }

        composable(Route.Login.path) {
            LoginScreen(nav = nav, vm = vm)
        }

        composable(Route.Home.path) {
            HomeScreen(
                nav = nav,
                vm = vm,
                onLogoutDone = {
                    vm.logout()
                    nav.navigate(Route.Login.path) {
                        popUpTo(Route.Home.path) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.Register.path) {
            RegisterScreen(nav)
        }

        composable(Route.Iot.path) {
            IotScreen(nav)
        }

        composable(Route.ForgotPassword.path) {
            ForgotPasswordScreen(nav)
        }


        composable(
            route = Route.ResetPassword.path + "?email={email}&code={code}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType; defaultValue = "" },
                navArgument("code") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            val code = backStackEntry.arguments?.getString("code")
            ResetPasswordScreen(nav, email, code)
        }

        composable(Route.CrudUsuario.path) {
            CrudMenuScreen(nav)
        }

        composable(Route.IngresarUsuario.path) {
            IngresarUsuarioScreen(nav)
        }

        composable(Route.ListarUsuarios.path) {
            ListarUsuariosScreen(nav)
        }

        composable(Route.Desarrollador.path) {
            DesarrolladorScreen(nav)
        }

        composable(
            route = "editarUsuario/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("id")
            if (userId != null) {
                EditarUsuarioScreen(nav, userId)
            }
        }
    }
}

@Composable
fun SplashLottie() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading)
    )
    val animState = animateLottieCompositionAsState(
        composition,
        iterations = Int.MAX_VALUE
    )

    Box(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        if (composition == null) {
            CircularProgressIndicator()
        } else {
            LottieAnimation(
                composition = composition,
                progress = { animState.progress },
                modifier = Modifier.size(220.dp)
            )
        }
    }
}
