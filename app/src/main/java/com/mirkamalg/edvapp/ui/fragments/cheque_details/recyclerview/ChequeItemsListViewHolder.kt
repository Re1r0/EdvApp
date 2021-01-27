package com.mirkamalg.edvapp.ui.fragments.cheque_details.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.mirkamalg.edvapp.databinding.ItemChequeItemsBinding
import com.mirkamalg.edvapp.model.data.ChequeItemData

/**
 * Created by Mirkamal on 27 January 2021
 */
class ChequeItemsListViewHolder private constructor(private val binding: ItemChequeItemsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: ChequeItemData) {
        binding.apply {
            textViewItemName.text = data.itemName.toString()
            textViewItemCount.text = data.itemQuantity.toString()
            textViewItemPrice.text = data.itemPrice.toString()
            textViewItemResult.text = data.itemSum.toString()
        }
    }

    companion object {
        fun from(binding: ItemChequeItemsBinding): ChequeItemsListViewHolder {
            return ChequeItemsListViewHolder(binding)
        }
    }


}