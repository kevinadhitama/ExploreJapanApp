package com.example.explorejapan.listener

interface ItemClickListener<T> {
    fun onItemClickListener(position: Int, item: T)
}