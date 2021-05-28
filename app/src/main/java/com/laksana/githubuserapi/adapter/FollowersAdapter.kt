package com.laksana.githubuserapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.laksana.githubuserapi.GlideApp
import com.laksana.githubuserapi.R
import com.laksana.githubuserapi.model.User
import kotlinx.android.synthetic.main.item_user.view.*

class FollowersAdapter(private val followerItem: ArrayList<User>): RecyclerView.Adapter<FollowersAdapter.ViewHolder>() {

    fun setData (followers : ArrayList<User>) {
        followerItem.clear()
        followerItem.addAll(followers)
        followerItem.count()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatar = itemView.img_avatar
        private val username = itemView.tv_username

        internal fun bind(follower: User) {
            GlideApp.with(itemView.context).load(follower.avatar).into(avatar)
            username.text = follower.username

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val follower = followerItem[position]
        holder.bind(follower)
    }

    override fun getItemCount(): Int {
        return followerItem.size
    }

}