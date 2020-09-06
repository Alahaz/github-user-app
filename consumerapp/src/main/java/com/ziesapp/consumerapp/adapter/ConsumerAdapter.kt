package com.ziesapp.consumerapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ziesapp.consumerapp.R
import com.ziesapp.consumerapp.model.User
import kotlinx.android.synthetic.main.list_profile.view.*

class ConsumerAdapter(private val activity: Activity) :
    RecyclerView.Adapter<ConsumerAdapter.UserViewHolder>() {
    var listFav = ArrayList<User>()
        set(listFav) {
            if (listFav.size > 0) {
                this.listFav.clear()
            }
            this.listFav.addAll(listFav)
            notifyDataSetChanged()
        }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {
                tv_username.text = user.username
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .into(img_photo)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_profile, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
        holder.bind(listFav[position])

    override fun getItemCount(): Int = this.listFav.size
}