package es.unex.giiis.asee.spanishweather.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.unex.giiis.asee.spanishweather.api.conexionAPI
import es.unex.giiis.asee.spanishweather.database.RepositoryLocalidades
import es.unex.giiis.asee.spanishweather.database.RepositoryUsers
import es.unex.giiis.asee.spanishweather.utils.CredentialCheck
import es.unex.giiis.asee.spanishweather.database.SpanishWeatherDatabase
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var repository : RepositoryUsers
    private lateinit var db: SpanishWeatherDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        db = SpanishWeatherDatabase.getInstance(applicationContext)!!
        repository = RepositoryUsers.getInstance(db.userDao())
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
                repository.setUserName(etUsuario.text.toString())
            }
        }
    }
    private fun subscribeUi() {
        repository.userSaved.observe(this)
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