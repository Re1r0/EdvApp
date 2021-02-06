package com.mirkamalg.edvapp.ui.fragments.expenses.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.mirkamalg.edvapp.databinding.ItemFavoriteGoodsListBinding

/**
 * Created by Mirkamal on 06 February 2021
 */
class FavoriteGoodsListViewHolder private constructor(private val binding: ItemFavoriteGoodsListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: String) {
        binding.textViewItemName.text = data
    }

    companion object {
        fun from(binding: ItemFavoriteGoodsListBinding): FavoriteGoodsListViewHolder {
            return FavoriteGoodsListViewHolder(binding)
        }
    }
}