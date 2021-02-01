package com.mirkamalg.edvapp.ui.fragments.vat.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.mirkamalg.edvapp.model.data.VATListItemData

/**
 * Created by Mirkamal on 01 February 2021
 */
class VatItemDiffCallback : DiffUtil.ItemCallback<VATListItemData>() {
    override fun areItemsTheSame(oldItem: VATListItemData, newItem: VATListItemData): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: VATListItemData, newItem: VATListItemData): Boolean {
        return oldItem == newItem
    }
}