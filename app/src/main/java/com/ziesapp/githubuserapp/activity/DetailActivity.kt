package com.ziesapp.githubuserapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ziesapp.githubuserapp.R
import com.ziesapp.githubuserapp.adapter.PagerAdapter
import com.ziesapp.githubuserapp.data.User
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val PARCEL = "Parcel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setUser()
    }

    private fun setUser() {
        val getData = intent.getParcelableExtra<User>(PARCEL)

        tv_name.text = getData?.name
        tv_username.text = getData?.username
        tv_company.text = getData?.company
        tv_location.text = getData?.location
        tv_repo.text = getData?.repo
        tv_bio.text = getData?.bio
        tv_web.text = getData?.web
        Glide.with(this)
            .load(getData?.avatar)
            .into(img_foto)
        val user = User()
        val pagerAdapter = PagerAdapter(
            this,
            supportFragmentManager
        )
        pagerAdapter.username = getData?.username
        view_pager.adapter = pagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }

}