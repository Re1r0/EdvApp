package com.mirkamalg.edvapp.ui.fragments.cheque_details.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.mirkamalg.edvapp.model.data.ChequeItemData

/**
 * Created by Mirkamal on 27 January 2021
 */
class ChequeItemDiffCallback : DiffUtil.ItemCallback<ChequeItemData>() {
    override fun areItemsTheSame(oldItem: ChequeItemData, newItem: ChequeItemData): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: ChequeItemData, newItem: ChequeItemData): Boolean {
        return oldItem == newItem
    }
}