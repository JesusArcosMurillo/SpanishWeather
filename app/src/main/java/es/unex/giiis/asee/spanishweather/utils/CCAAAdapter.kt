package es.unex.giiis.asee.spanishweather.utils

import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import es.unex.giiis.asee.spanishweather.R
import es.unex.giiis.asee.spanishweather.databinding.DropdownItemBinding
import es.unex.giiis.asee.spanishweather.databinding.RecyclerVerticalBinding

class CCAAAdapter(
    context: Context,
    private val options: List<CCAAOption>,
) : ArrayAdapter<CCAAOption>(context, R.layout.dropdown_item, options) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = DropdownItemBinding.inflate(LayoutInflater.from(context))
        val option = getItem(position)

        binding.imageCcaa.setImageResource(option!!.imageResId)
        binding.tvCcaa.text = option.name

        return binding.root
    }
}