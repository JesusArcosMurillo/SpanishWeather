package es.unex.giiis.asee.spanishweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.unex.giiis.asee.spanishweather.R
import es.unex.giiis.asee.spanishweather.databinding.ActivityLoginBinding
import es.unex.giiis.asee.spanishweather.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

