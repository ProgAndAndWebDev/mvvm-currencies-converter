package com.dmide.currencies.ui

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.dmide.currencies.databinding.ListItemBinding
import com.dmide.currencies.model.Currency
import com.dmide.currencies.util.*
import com.jakewharton.rxbinding2.widget.RxTextView
import com.squareup.picasso.Picasso

private const val defaultPrecision = 2
private var selectedCurrencyDisplayedPrecision: Int? = 2

class CurrencyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    val nameLiveData = MutableLiveData<String>()
    val valueLiveData = MutableLiveData<String>()
    private val currenciesViewModel = (binding.root.context as CurrenciesActivity).viewModel

    private var currency: Currency? = null

    init {
        setupTextWatcher()
    }

    fun bind(currency: Currency) {
        this.currency = currency
        nameLiveData.value = currency.name

        if (currency.name != currenciesViewModel.selectedCurrencyName) {
            valueLiveData.value = "%.${defaultPrecision}f".format(currency.value)
        } else if (!binding.value.hasFocus()) {
            val newValue = currency.value.toShortString(selectedCurrencyDisplayedPrecision)
            valueLiveData.setIfDiffers(newValue)
        }

        binding.layout.setOnClickListener {
            binding.value.apply {
                requestFocus()
                setSelection(text.length)
                context.showKeyboard()
            }
        }
        binding.value.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                selectedCurrencyDisplayedPrecision = defaultPrecision
                currenciesViewModel.onCurrencySelected(currency)
            }
        }

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
                val stringValue = text.toString()
                selectedCurrencyDisplayedPrecision = stringValue.numberOfDecimals()
                valueLiveData.value = stringValue
                stringValue.toFloatOrNull().let { value ->
                    val newValue = value ?: 0f
                    currency?.let { c ->
                        c.value = newValue
                        val newBaseValue = newValue / c.rate
                        currenciesViewModel.onAmountUpdated(newBaseValue)
                    }
                }
            }
    }
}