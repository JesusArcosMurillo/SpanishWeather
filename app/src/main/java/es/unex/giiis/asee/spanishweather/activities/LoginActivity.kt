package es.unex.giiis.asee.spanishweather.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.unex.giiis.asee.spanishweather.activities.HomeActivity
import es.unex.giiis.asee.spanishweather.database.CredentialCheck
import es.unex.giiis.asee.spanishweather.database.SpanishWeatherDatabase
import es.unex.giiis.asee.spanishweather.database.Usuario
import es.unex.giiis.asee.spanishweather.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: SpanishWeatherDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        db = SpanishWeatherDatabase.getInstance(applicationContext)!!
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

    private fun comprobarCredenciales(){
        with(binding){
            val usuario = Usuario(nombre = etUsuario.text.toString(), contraseña = etContrasena.text.toString())
            val check = CredentialCheck.login(etUsuario.text.toString(), etContrasena.text.toString())
            Log.i("TAG", etContrasena.text.toString())
            Log.i("TAG", usuario.contraseña)
            if (check.fail)
                notifyInvalidCredentials(check.msg)
            else //si las credenciales están bien, comprobamos que exista el usuario en la bd
                buscarBD(usuario)
        }
    }

    private fun buscarBD(usuario: Usuario){
            lifecycleScope.launch{
                val user = db?.userDao()?.buscarUsuario(usuario.nombre) //buscamos en la base de datos el usuario
                if (user != null) { //si se encuentra, comprobaremos que la contraseña es correcta
                    Log.i("TAG", usuario.contraseña)
                    Log.i("TAG", user.contraseña)
                    val contrasenasIguales = CredentialCheck.contrasenasIguales(usuario.contraseña, user.contraseña)
                    if (contrasenasIguales)
                        navigateToHomeActivity(user!!)
                    else
                        notifyInvalidCredentials("La contraseña introducida no coincide.")
                }
                else
                    notifyInvalidCredentials("Error: usuario no encontrado en la BD") //si no se encuentra, mostramos mensaje de error
             }
    }

    private fun navigateToHomeActivity(usuario: Usuario) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("USUARIO", usuario)
        startActivity(intent)
    }

    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}