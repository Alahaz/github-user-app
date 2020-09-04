package com.ziesapp.githubuserapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ziesapp.githubuserapp.CustomOnItemClickListener
import com.ziesapp.githubuserapp.R
import com.ziesapp.githubuserapp.activity.DetailActivity
import com.ziesapp.githubuserapp.model.User
import kotlinx.android.synthetic.main.activity_detail.view.tv_username
import kotlinx.android.synthetic.main.list_profile.view.*

class FavoriteAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavoriteAdapter.UserViewHolder>() {
    var listFav = ArrayList<User>()
        set(listFav) {
            if (listFav.size > 0) {
                this.listFav.clear()
            }
            this.listFav.addAll(listFav)
            notifyDataSetChanged()
        }

    fun addItem(user: User) {
        this.listFav.add(user)
        notifyItemInserted(this.listFav.size - 1)
    }

    fun updateItem(position: Int, user: User) {
        this.listFav[position] = user
        notifyItemChanged(position, user)
    }

    fun removeItem(position: Int) {
        this.listFav.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFav.size)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {
                tv_username.text = user.username
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .into(img_photo)
                profile.setOnClickListener {
                    CustomOnItemClickListener(
                        adapterPosition,
                        object :
                            CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(v: View, position: Int) {
                                val intent = Intent(activity, DetailActivity::class.java)
                                intent.putExtra(DetailActivity.PARCEL, user)
                                activity.startActivity(intent)
                            }
                        }
                    )
                }
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