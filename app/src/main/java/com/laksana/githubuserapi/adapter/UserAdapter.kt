package com.laksana.githubuserapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.laksana.githubuserapi.GlideApp
import com.laksana.githubuserapi.OnItemClickCallback
import com.laksana.githubuserapi.R
import com.laksana.githubuserapi.model.User
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(private var userList: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    var listener: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.listener = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.item_user, parent, false)

        return this.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUserName = itemView.tv_username as TextView

        fun bind(items: User) {
            tvUserName.text = items.username
            GlideApp.with(itemView.context).load(items.avatar)
                .into(itemView.img_avatar)

            itemView.setOnClickListener { listener?.onItemClicked(items) }
        }
    }
}
