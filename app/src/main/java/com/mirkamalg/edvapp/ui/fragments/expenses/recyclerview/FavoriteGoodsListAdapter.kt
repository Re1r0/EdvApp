package com.mirkamalg.edvapp.ui.fragments.expenses.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mirkamalg.edvapp.databinding.ItemFavoriteGoodsListBinding

/**
 * Created by Mirkamal on 06 February 2021
 */
class FavoriteGoodsListAdapter :
    ListAdapter<String, FavoriteGoodsListViewHolder>(StringItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteGoodsListViewHolder {
        val binding =
            ItemFavoriteGoodsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteGoodsListViewHolder.from(binding)
    }

    override fun onBindViewHolder(holder: FavoriteGoodsListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}