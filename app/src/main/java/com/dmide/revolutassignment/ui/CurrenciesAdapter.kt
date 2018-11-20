package com.dmide.revolutassignment.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dmide.revolutassignment.R
import com.dmide.revolutassignment.databinding.ListItemBinding
import com.dmide.revolutassignment.model.Currency

class CurrenciesAdapter(currenciesActivity: CurrenciesActivity, currenciesViewModel: CurrenciesViewModel) :
    RecyclerView.Adapter<CurrencyViewHolder>() {

    private var currencyList: List<Currency> = listOf()
    private var pendingListUpdate: List<Currency>? = null
    private var isScrolling: Boolean = false

    init {
        currenciesViewModel.currenciesLiveData.observe(currenciesActivity, Observer { newList ->
            if (isScrolling) {
                // This is more verbose than for example 'skipWhile {isScrolling}'
                // but preserves the pending value in case of connection lost.
                // Although there should be the way to do it using RX, I can't come up with a simple one.
                pendingListUpdate = newList
                return@Observer
            }
            dispatchListUpdate(newList)
        })

        currenciesViewModel.scrollStateLiveData.observe(currenciesActivity, Observer { scrollState ->
            isScrolling = scrollState != RecyclerView.SCROLL_STATE_IDLE
            if (!isScrolling) {
                pendingListUpdate?.let {
                    dispatchListUpdate(it)
                    pendingListUpdate = null
                }
            }
        })

    }

    private fun dispatchListUpdate(newList: List<Currency>) {
        // the list is small enough to have an update time way under 16ms (1-2ms on Nexus 5X), so no need for async version
        val diffResult = DiffUtil.calculateDiff(ListDiffUtilCallback(currencyList, newList))
        currencyList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding: ListItemBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_item, parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(currencyList[position])
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    class ListDiffUtilCallback(val newList: List<Currency>, val oldList: List<Currency>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].name == newList[newItemPosition].name
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].rate == newList[newItemPosition].rate
        }
    }
}
