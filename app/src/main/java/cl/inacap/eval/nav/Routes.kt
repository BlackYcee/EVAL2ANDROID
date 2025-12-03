package cl.inacap.eval.nav

sealed class Route(val path: String) {
    data object Login : Route("login")
    data object Register : Route("register")
    data object Home : Route("home")

    data object Iot : Route("iot")

    data object ForgotPassword : Route("forgot_password")
    data object ResetPassword : Route("reset_password")

    data object CrudUsuario : Route("crudUsuario")
    data object IngresarUsuario : Route("ingresarUsuario")
    data object ListarUsuarios : Route("listarUsuarios")

    data object EditarUsuario : Route("editarUsuario/{id}")

    data object Desarrollador : Route("desarrollador")

}
