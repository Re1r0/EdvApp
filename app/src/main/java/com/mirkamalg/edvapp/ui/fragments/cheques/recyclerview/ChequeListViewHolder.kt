package com.mirkamalg.edvapp.ui.fragments.cheques.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.ItemChequesListBinding
import com.mirkamalg.edvapp.model.entities.ChequeEntity

/**
 * Created by Mirkamal on 24 January 2021
 */
class ChequeListViewHolder private constructor(
    private val binding: ItemChequesListBinding,
    private val itemClickListener: (ChequeEntity) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: ChequeEntity) {

        itemView.setOnClickListener {
            itemClickListener(data)
        }

        val context = itemView.context

        data.sum?.let {
            binding.textViewChequeSpending.text =
                context.getString(R.string.msg_money_amount_template, it.toString())
        }
        data.vatResult?.let {
            binding.textViewChequeVAT.text =
                context.getString(R.string.msg_money_amount_template, it.toString())
        }
        data.createdAtUtc?.let {
            binding.textViewChequeDateTime.text = it.toString()
        }
    }

    companion object {
        fun from(
            binding: ItemChequesListBinding,
            itemClickListener: (ChequeEntity) -> Unit
        ): ChequeListViewHolder {
            return ChequeListViewHolder(binding, itemClickListener)
        }
    }
}