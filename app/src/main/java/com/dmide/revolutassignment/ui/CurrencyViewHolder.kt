package com.dmide.revolutassignment.ui

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.dmide.revolutassignment.common.CropCircleTransformation
import com.dmide.revolutassignment.databinding.ListItemBinding
import com.dmide.revolutassignment.model.Currency
import com.squareup.picasso.Picasso


class CurrencyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    val name = MutableLiveData<String>()
    val rate = MutableLiveData<String>()

    fun bind(currency: Currency) {
        name.value = currency.name
        rate.value = "%.2f".format(currency.rate)

        Picasso.get().load("file:///android_asset/icons_currency/${currency.name.toLowerCase()}.png")
            .transform(CropCircleTransformation())
            .into(binding.icon);

        binding.viewHolder = this
    }
}