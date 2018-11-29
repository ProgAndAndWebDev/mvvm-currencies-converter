package com.dmide.revolutassignment.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dmide.revolutassignment.R
import com.dmide.revolutassignment.databinding.ListItemBinding
import com.dmide.revolutassignment.model.Currency

class CurrenciesAdapter() : RecyclerView.Adapter<CurrencyViewHolder>() {

    private var currencyList: List<Currency> = listOf()

    fun dispatchListUpdate(newList: List<Currency>) {
        // the list is small enough to have an update time way under 16ms (1-2ms on Nexus 5X), so no need for async version
        val diffResult = DiffUtil.calculateDiff(ListDiffUtilCallback(newList, currencyList))
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
            return oldList[oldItemPosition].value == newList[newItemPosition].value
        }
    }
}
