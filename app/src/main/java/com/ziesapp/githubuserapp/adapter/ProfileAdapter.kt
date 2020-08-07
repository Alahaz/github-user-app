package com.ziesapp.githubuserapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ziesapp.githubuserapp.R
import com.ziesapp.githubuserapp.data.User
import kotlinx.android.synthetic.main.list_profile.view.*
import kotlinx.android.synthetic.main.list_profile.view.tv_username

class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.profileViewHolder>() {

    private val listProfile = ArrayList<User>()

    private var onItemCallBack: OnItemCallBack? = null

    interface OnItemCallBack {
        fun onItemClicked(data: User)
    }

    fun setOnItemCallback(onItemCallBack: OnItemCallBack) {
        this.onItemCallBack = onItemCallBack
    }

    fun setData(items: ArrayList<User>) {
        listProfile.clear()
        listProfile.addAll(items)
        notifyDataSetChanged()
    }

    inner class profileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: User) {
            with(itemView) {
                tv_username.text = user.username
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .into(img_photo)
                itemView.setOnClickListener {
                    onItemCallBack?.onItemClicked(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): profileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_profile, parent, false)
        return profileViewHolder(view)
    }

    override fun getItemCount(): Int = listProfile.size

    override fun onBindViewHolder(holder: profileViewHolder, position: Int) =
        holder.bind(listProfile[position])

}