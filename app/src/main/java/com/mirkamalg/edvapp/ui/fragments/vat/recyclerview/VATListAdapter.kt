package com.mirkamalg.edvapp.ui.fragments.vat.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mirkamalg.edvapp.databinding.ItemVatListBinding
import com.mirkamalg.edvapp.model.data.VATListItemData

/**
 * Created by Mirkamal on 01 February 2021
 */
class VATListAdapter : ListAdapter<VATListItemData, VATListViewHolder>(VatItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VATListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVatListBinding.inflate(inflater, parent, false)
        return VATListViewHolder.from(binding)
    }

    override fun onBindViewHolder(holder: VATListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}