package es.unex.giiis.asee.spanishweather.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import es.unex.giiis.asee.spanishweather.SpanishWeatherApplication
import es.unex.giiis.asee.spanishweather.api.conexionAPI
import es.unex.giiis.asee.spanishweather.database.RepositoryLocalidades
import es.unex.giiis.asee.spanishweather.database.RepositoryUsers
import es.unex.giiis.asee.spanishweather.utils.CredentialCheck
import es.unex.giiis.asee.spanishweather.database.SpanishWeatherDatabase
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.databinding.ActivityLoginBinding
import es.unex.giiis.asee.spanishweather.fragments.DetailViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels { LoginViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (this.application as SpanishWeatherApplication).appContainer //todas las activities tienen una referencia a la app

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpListeners()
    }


    private fun setUpListeners() {
        with(binding) {
            btSign.setOnClickListener {
                comprobarCredenciales()
            }
            etRegistro.setOnClickListener{
                navegarPantallaRegistro()
            }
        }
    }

    private fun navegarPantallaRegistro() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun comprobarCredenciales() {
        with(binding) {
            val check =
                CredentialCheck.login(etUsuario.text.toString(), etContrasena.text.toString())
            if (check.fail)
                notifyInvalidCredentials(check.msg)
            else { //si las credenciales están bien, comprobamos que exista el usuario en la bd
                val usuario = Usuario(contraseña = etContrasena.text.toString(), userName = etUsuario.text.toString())
                subscribeUi()
                viewModel.user = usuario
            }
        }
    }
    private fun subscribeUi() {
        viewModel.userSaved.observe(this)
        { user -> if (user != null) { //si se encuentra, comprobaremos que la contraseña es correcta
                val contrasenasIguales = CredentialCheck.contrasenasIguales(binding.etContrasena.text.toString(), user.contraseña)
            if (contrasenasIguales) {
                navigateToHomeActivity(user!!)
            }else {
                notifyInvalidCredentials("La contraseña introducida no coincide.")
            }
        }
        else
            notifyInvalidCredentials("Error: usuario no encontrado en la BD") //si no se encuentra, mostramos mensaje de error
        }
    }


    private fun navigateToHomeActivity(usuario: Usuario) {
        HomeActivity.start(this, usuario)
    }

    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}