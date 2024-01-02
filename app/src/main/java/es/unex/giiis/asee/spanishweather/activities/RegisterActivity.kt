package es.unex.giiis.asee.spanishweather.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.unex.giiis.asee.spanishweather.database.RepositoryUsers
import es.unex.giiis.asee.spanishweather.utils.CredentialCheck
import es.unex.giiis.asee.spanishweather.database.SpanishWeatherDatabase
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: SpanishWeatherDatabase
    private lateinit var repository : RepositoryUsers
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        db = SpanishWeatherDatabase.getInstance(applicationContext)!!
        repository = RepositoryUsers.getInstance(db.userDao())
        setContentView(binding.root)
        setUpListeners()
    }

    private fun setUpListeners() {
        with(binding) {
            btReg.setOnClickListener {
                comprobarCredenciales()
            }
        }
    }

    private fun comprobarCredenciales(){
        with(binding){
            val nombre=etNombre.text.toString()
            val apellido=etApellido.text.toString()
            val contrasena= etContrasena.text.toString()
            val usuario=etUsuario.text.toString()
            val email=etEmail.text.toString()
            val user = Usuario (usuario, nombre, apellido, contrasena,
                email)

            val check = CredentialCheck.join(nombre, apellido,
                contrasena, etContrasenaX2.text.toString(),usuario,email)

            if (check.fail)
                notifyInvalidCredentials(check.msg)
            else
                registrarUsuario(user)
        }
    }

    private fun registrarUsuario (usuario: Usuario){
        with(binding) {
            lifecycleScope.launch{
                val error = repository.insertarUsuario(usuario)
                navegarMainActivity(usuario)
            }
        }
    }

     fun navegarMainActivity(usuario: Usuario) {
         HomeActivity.start(this, usuario)

    }

    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
