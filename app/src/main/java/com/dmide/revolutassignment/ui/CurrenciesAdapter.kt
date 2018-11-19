package com.dmide.revolutassignment.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dmide.revolutassignment.R
import com.dmide.revolutassignment.common.CurrenciesApplication
import com.dmide.revolutassignment.databinding.ListItemBinding
import com.dmide.revolutassignment.model.Currency

class CurrenciesAdapter(currenciesActivity: CurrenciesActivity, currenciesViewModel: CurrenciesViewModel) :
    RecyclerView.Adapter<CurrencyViewHolder>() {

    var currencyList: List<Currency> = listOf()

    init {
        currenciesViewModel.currenciesLiveData.observe(currenciesActivity, Observer {
            val diffResult = DiffUtil.calculateDiff(ListDiffUtilCallback(currencyList, it))
            currencyList = it
            diffResult.dispatchUpdatesTo(this)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding: ListItemBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_item, parent, false)
        val postViewHolder = CurrencyViewHolder(binding)
        (parent.context.applicationContext as CurrenciesApplication).component.inject(postViewHolder)
        return postViewHolder
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
