package com.mirkamalg.edvapp.ui.fragments.cheque_details.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mirkamalg.edvapp.databinding.ItemChequeItemsBinding
import com.mirkamalg.edvapp.model.data.ChequeItemData

/**
 * Created by Mirkamal on 27 January 2021
 */
class ChequeItemsListAdapter :
    ListAdapter<ChequeItemData, ChequeItemsListViewHolder>(ChequeItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChequeItemsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChequeItemsBinding.inflate(inflater, parent, false)
        return ChequeItemsListViewHolder.from(binding)
    }

    override fun onBindViewHolder(holder: ChequeItemsListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}