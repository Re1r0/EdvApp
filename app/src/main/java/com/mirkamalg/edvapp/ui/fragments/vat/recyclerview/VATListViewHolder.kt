package com.mirkamalg.edvapp.ui.fragments.vat.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.ItemVatListBinding
import com.mirkamalg.edvapp.model.data.VATListItemData

/**
 * Created by Mirkamal on 01 February 2021
 */
class VATListViewHolder private constructor(private val binding: ItemVatListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: VATListItemData) {
        binding.apply {
            textViewItemName.text = item.name
            textViewItemPrice.text = itemView.context.getString(
                R.string.msg_money_amount_template,
                item.price.toString()
            )
            textViewItemVat.text =
                itemView.context.getString(R.string.msg_money_amount_template, item.vat.toString())
        }
    }

    companion object {
        fun from(binding: ItemVatListBinding): VATListViewHolder {
            return VATListViewHolder(binding)
        }
    }
}