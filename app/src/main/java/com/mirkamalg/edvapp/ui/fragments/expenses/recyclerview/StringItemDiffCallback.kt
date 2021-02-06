package com.mirkamalg.edvapp.ui.fragments.expenses.recyclerview

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Mirkamal on 06 February 2021
 */
class StringItemDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}