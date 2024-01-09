package es.unex.giiis.asee.spanishweather.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import es.unex.giiis.asee.spanishweather.R
import es.unex.giiis.asee.spanishweather.SpanishWeatherApplication
import es.unex.giiis.asee.spanishweather.activities.HomeViewModel
import es.unex.giiis.asee.spanishweather.api.conexionAPI
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.databinding.FragmentDetailBinding
import es.unex.giiis.asee.spanishweather.utils.UserProvider
import kotlinx.coroutines.launch
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class DetailFragment : Fragment() {
    private val viewModel: DetailViewModel by viewModels { DetailViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var _binding:FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()

    companion object {
        fun newInstance(localidad: Localidad): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putSerializable("localidad", localidad)
            fragment.arguments = args
            return fragment
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) {
                user -> viewModel.user = user
        }
        val localidad = args.localidad
        viewModel.localidad = localidad

        viewModel.toast.observe(viewLifecycleOwner) {
                text -> text?.let {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            viewModel.onToastShown()
        }
        }

        val hora = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH") // Utiliza "HH" para obtener solo las horas
        var horaActual = hora.format(formatter).toInt()  //sacamos la hora actual para poder acceder a la hora adecuada en el vector

        binding.puebloFav.isChecked = localidad.is_favourite
        binding.puebloFav.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.onFavoriteButtonClick()
                } else {
                    viewModel.onNoFavoriteButtonClick()
                }
            }
        binding.tvNombre.text = localidad.location.name
        binding.tvTpactual.text = localidad.current.temp_c.toInt().toString() //le quitamos los decimales
        if (localidad.current.cloud<10){
            binding.textView6.text="Sunny"
        }else{
            if (localidad.current.cloud>10 && localidad.current.cloud < 50){
                binding.textView6.text="Partly cloudy"
            }else{
                binding.textView6.text="Overcast"
            }
        }

        binding.textView3.text = localidad.current.wind_dir
        binding.textView7.text = "${localidad.current.wind_kph}km/h"
        binding.textView5.text = "${localidad.current.humidity}%"
        binding.textView4.text = "${localidad.current.precip_mm}mm"
        binding.textView8.text = "${localidad.current.cloud}%"
        binding.textView9.text = "${localidad.current.feelslike_c}ºC"
        val customFont = ResourcesCompat.getFont(requireContext(), R.font.bebas_neue)
        binding.textView3.typeface = customFont
        binding.textView7.typeface = customFont
        binding.textView5.typeface = customFont
        binding.textView4.typeface = customFont
        binding.textView8.typeface = customFont
        binding.textView9.typeface = customFont
    }

    private fun obtenerDiaSemana(fechaString: String): String {
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        try {

            val fecha = formatoFecha.parse(fechaString) // convertimos la cadena de fecha a un objeto Date
            val calendario = Calendar.getInstance() // se obtiene el día de la semana
            calendario.time = fecha
            val diaSemana = calendario.get(Calendar.DAY_OF_WEEK)
            val nombresDias = arrayOf("", "Domingo", "Lunes",
                "Martes", "Miércoles", "Jueves", "Viernes", "Sábado") // mapear el número del día de la semana a un nombre
            val dia = nombresDias[diaSemana]

            return dia.take(3) //devolvemos las 3 primeras letras
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return "ERROR AL OBTENER DIA DE LA SEMANA"
    }

    fun obtenerHoraDesdeCadenaTiempo(cadenaTiempo: String): String {
        val formatoEntrada = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val formatoSalida = SimpleDateFormat("HH", Locale.getDefault())

        try {
            val fechaHora = formatoEntrada.parse(cadenaTiempo)
            return formatoSalida.format(fechaHora)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return "ERROR AL MAPEAR LA HORA"
    }

    private fun convertirFormato24Horas(horaAMPM: String): String {
        val AMoPM = horaAMPM.takeLast(2)
        var horaSalida = "ERROR AL CONVERTIR AM/PM"

        if (AMoPM.equals("AM", ignoreCase = true)) {
                horaSalida = horaAMPM.take(5) //si es AM, no habrá que realizar operaciones especiales
        } else {
            if (AMoPM.equals("PM", ignoreCase = true)) {
                val calculo = horaAMPM.take(2).toInt() + 12 // si es PM, sumamos doce a las horas
                horaSalida = "$calculo${horaAMPM.substring(2, 5)}"
            }
        }
        return horaSalida
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}