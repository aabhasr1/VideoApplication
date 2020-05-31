package `in`.aabhas.videoapplication.ui.screen2

import `in`.aabhas.videoapplication.R
import `in`.aabhas.videoapplication.databinding.AdapterCompressionItemBinding
import `in`.aabhas.videoapplication.utils.Constants
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil


class CompressionAdapter(context: Context, val model: Screen2ViewModel) :
    ArrayAdapter<Constants.COMPRESSION_FORMATS>(
        context,
        R.layout.adapter_compression_item,
        R.id.text,
        enumValues<Constants.COMPRESSION_FORMATS>()
    ) {

//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        return createItemView(position, convertView, parent)
//    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = DataBindingUtil.inflate<AdapterCompressionItemBinding>(
            LayoutInflater.from(context),
            R.layout.adapter_compression_item,
            parent,
            false
        )
        binding.item = getItem(position)
        binding.model = model

        return binding.root
    }
}