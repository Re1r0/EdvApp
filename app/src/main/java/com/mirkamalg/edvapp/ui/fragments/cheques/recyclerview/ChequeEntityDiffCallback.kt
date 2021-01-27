package com.mirkamalg.edvapp.ui.fragments.cheques.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.mirkamalg.edvapp.model.entities.ChequeEntity

/**
 * Created by Mirkamal on 24 January 2021
 */
class ChequeEntityDiffCallback : DiffUtil.ItemCallback<ChequeEntity>() {
    override fun areItemsTheSame(oldItem: ChequeEntity, newItem: ChequeEntity): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: ChequeEntity, newItem: ChequeEntity): Boolean {
        return oldItem == newItem
    }

}