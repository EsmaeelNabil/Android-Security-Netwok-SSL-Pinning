package com.example.securedapplication.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.securedapplication.databinding.ListItemBinding
import com.example.securedapplication.network.DummyListResponse


class MyListAdapter() :
    ListAdapter<DummyListResponse.DummyListResponseItem, MyListAdapter.ItemHolder>(
        LatestNewsDiffUtil
    ) {

    private object LatestNewsDiffUtil :
        DiffUtil.ItemCallback<DummyListResponse.DummyListResponseItem>() {
        override fun areItemsTheSame(
            oldItem: DummyListResponse.DummyListResponseItem,
            newItem: DummyListResponse.DummyListResponseItem
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: DummyListResponse.DummyListResponseItem,
            newItem: DummyListResponse.DummyListResponseItem
        ) = oldItem.id == newItem.id
    }

    inner class ItemHolder(private val binder: ListItemBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bindViews(item: DummyListResponse.DummyListResponseItem) {
            binder.image.load(item.avatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bindViews(getItem(position))
    }
}