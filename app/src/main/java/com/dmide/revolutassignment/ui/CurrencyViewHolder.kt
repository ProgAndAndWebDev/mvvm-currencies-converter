package com.dmide.revolutassignment.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.dmide.revolutassignment.databinding.ListItemBinding
import com.dmide.revolutassignment.model.Currency
import javax.inject.Inject

class CurrencyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    @Inject
    lateinit var context: Context //TODO load icons using Picasso.with(context)

    val name = MutableLiveData<String>()
    val rate = MutableLiveData<String>()

    fun bind(currency: Currency) {
        name.value = currency.name
        rate.value = "%.2f".format(currency.rate)
        binding.viewHolder = this
    }
}