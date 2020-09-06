package com.ziesapp.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ziesapp.consumerapp.R
import com.ziesapp.consumerapp.model.User
import kotlinx.android.synthetic.main.list_profile.view.*

class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

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

    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_profile, parent, false)
        return ProfileViewHolder(view)
    }

    override fun getItemCount(): Int = listProfile.size

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) =
        holder.bind(listProfile[position])

}