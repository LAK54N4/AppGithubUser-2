package com.laksana.githubuserapi

import com.laksana.githubuserapi.model.User

interface OnItemClickCallback {
    fun onItemClicked(data: User)
}
