package es.unex.giiis.asee.spanishweather.utils

import java.util.regex.Pattern

class CredentialCheck private constructor() {

    var fail: Boolean = false
    var msg: String = ""

    companion object{

        private val TAG = CredentialCheck::class.java.canonicalName

        private val checks = arrayOf(
            CredentialCheck().apply {
                fail = false
                msg = "Credenciales correctas."
            },
            CredentialCheck().apply {
                fail = true
                msg = "Nombre o apellido inválido. Recuerda que no se admiten" +
                        " números ni caracteres especiales."
            },
            CredentialCheck().apply {
                fail = true
                msg = "Contraseña inválida. Recuerda que es necesario al menos," +
                        " un número, un caracter especial ('_', '-' o '.') y una mayúscula."
            },
            CredentialCheck().apply {
                fail = true
                msg = "Las contraseñas no coinciden."
            },
            CredentialCheck().apply {
                fail = true
                msg = "Usuario inválido. Se necesitan al menos 4 caracteres."
            },
            CredentialCheck().apply {
                fail = true
                msg = "Correo electrónico inválido. Se necesitan al menos 11 caracteres."
            }


        )

        fun login(usuario: String, password: String): CredentialCheck {
            return if (usuario.isBlank() || usuario.length < 4) checks[4]
            else if (password.isBlank() || password.length < 4) checks[2]
            else checks[0]
        }

        fun join(nombre: String, apellido: String, contrasena: String,contrasenaX2: String,
                 usuario: String, correo: String): CredentialCheck {
            return if (usuario.isBlank() || usuario.length < 4) checks[4]
            else if (!validarNombre(apellido)) checks[1]
            else if (contrasena.isBlank() || !validarContrasena(contrasena)) checks[2]
            else if (contrasenaX2.isBlank() || !validarContrasena(contrasenaX2)) checks[2]
            else if (!validarNombre(nombre)) checks[1]
            else if (contrasenaX2!=contrasena) checks[3]
            else if (correo.isBlank() || correo.length < 11) checks[5]
            else checks[0]
        }


        // Comprueba mediante una expresión regular, que una contraseña tiene
        // un número, un caracter especial ('_', '-' o '.') y una mayúscula

        fun validarContrasena(contrasena: String): Boolean {
            val expresionRegular =
                "^(?=.*[A-Z])(?=.*\\d)(?=.*[_\\-*.]).{4,}\$"
            val pat = Pattern.compile(expresionRegular)
            val matcher = pat.matcher(contrasena)

            return matcher.matches()
        }

        // Comprueba mediante una expresión regular, que un nombre no tiene
        // ni numeros ni caracteres especiales. Además, también se comprueba si la cadena está vacía
        fun validarNombre(nombre: String): Boolean {
            val expresionRegular = "^[a-zA-Z]+$"
            val pat = Pattern.compile(expresionRegular)
            val matcher = pat.matcher(nombre)

            return nombre.isNotEmpty() && matcher.matches()
        }

        fun contrasenasIguales(contrasena1: String, contrasena2: String): Boolean {
            return contrasena1 == contrasena2
        }
    }
}