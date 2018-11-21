package com.dmide.revolutassignment.ui

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.dmide.revolutassignment.util.CropCircleTransformation
import com.dmide.revolutassignment.util.toShortString
import com.dmide.revolutassignment.databinding.ListItemBinding
import com.dmide.revolutassignment.model.Currency
import com.jakewharton.rxbinding2.widget.RxTextView
import com.squareup.picasso.Picasso

class CurrencyViewHolder(val binding: ListItemBinding, private val currenciesViewModel: CurrenciesViewModel) :
    RecyclerView.ViewHolder(binding.root) {

    val nameLiveData = MutableLiveData<String>()
    val valueLiveData = MutableLiveData<String>()

    private var currency: Currency? = null

    init {
        setupTextWatcher()
    }

    fun bind(currency: Currency) {
        this.currency = currency
        nameLiveData.value = currency.name

        if (currency.name != currenciesViewModel.selectedCurrencyName) {
            valueLiveData.value = "%.2f".format(currency.value)
        } else if (!binding.value.hasFocus()) {
            valueLiveData.value = currency.value.toShortString()
        }

        binding.value.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) currenciesViewModel.onCurrencySelected(currency) }

        Picasso.get().load("file:///android_asset/icons_currency/${currency.name.toLowerCase()}.png")
            .transform(CropCircleTransformation())
            .into(binding.icon);

        binding.viewHolder = this
    }

    /*
        we don't need to dispose textChanges Observable, it will be GCed along with the View, ViewHolder and adapter
     */
    @SuppressLint("CheckResult")
    private fun setupTextWatcher() {
        RxTextView.textChanges(binding.value)
            .filter { binding.value.hasFocus() } // pass changes only from the currently edited view
            .filter { it.toString().toFloatOrNull() != currency?.value } // filter identical values
            .subscribe { text ->
                valueLiveData.value = text.toString()
                text.toString().toFloatOrNull().let { value ->
                    currency?.let { c ->
                        val newBaseValue = (value ?: 0f) / c.rate
                        currenciesViewModel.onAmountUpdated(newBaseValue)
                    }
                }
            }
    }
}