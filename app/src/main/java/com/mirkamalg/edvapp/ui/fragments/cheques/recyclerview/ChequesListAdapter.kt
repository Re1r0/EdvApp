package com.mirkamalg.edvapp.ui.fragments.cheques.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mirkamalg.edvapp.databinding.ItemChequesListBinding
import com.mirkamalg.edvapp.model.entities.ChequeEntity

/**
 * Created by Mirkamal on 24 January 2021
 */
class ChequesListAdapter(private val itemClickListener: (ChequeEntity) -> Unit) :
    ListAdapter<ChequeEntity, ChequeListViewHolder>(ChequeEntityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChequeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChequesListBinding.inflate(inflater, parent, false)
        return ChequeListViewHolder.from(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ChequeListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}