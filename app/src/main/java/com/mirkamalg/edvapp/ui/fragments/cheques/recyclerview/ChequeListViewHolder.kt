package com.mirkamalg.edvapp.ui.fragments.cheques.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.ItemChequesListBinding
import com.mirkamalg.edvapp.model.entities.ChequeEntity
import com.mirkamalg.edvapp.util.formatToString
import java.util.*

/**
 * Created by Mirkamal on 24 January 2021
 */
class ChequeListViewHolder private constructor(
    private val binding: ItemChequesListBinding,
    private val itemClickListener: (ChequeEntity) -> Unit,
    private val deleteListener: (ChequeEntity, Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: ChequeEntity) {

        itemView.setOnClickListener {
            itemClickListener(data)
        }

        binding.imageButtonDeleteCheque.setOnClickListener {
            deleteListener(data, adapterPosition)
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
            binding.textViewChequeDateTime.text =
                Date(data.createdAtUtc.times(1000)).formatToString()
        }
    }

    companion object {
        fun from(
            binding: ItemChequesListBinding,
            itemClickListener: (ChequeEntity) -> Unit,
            deleteListener: (ChequeEntity, Int) -> Unit
        ): ChequeListViewHolder {
            return ChequeListViewHolder(binding, itemClickListener, deleteListener)
        }
    }
}