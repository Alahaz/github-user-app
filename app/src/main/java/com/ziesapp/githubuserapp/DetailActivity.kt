package com.ziesapp.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class DetailActivity : AppCompatActivity() {

    companion object{
        const val PARCEL = "Parcel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imgFoto:CircleImageView = findViewById(R.id.img_foto)
        val tvNama:TextView = findViewById(R.id.tv_nama)
        val tvUsername:TextView = findViewById(R.id.tv_username)
        val tvCompany:TextView = findViewById(R.id.tv_company)
        val tvLocation:TextView = findViewById(R.id.tv_location)
        val tvFollowers:TextView = findViewById(R.id.tv_followers)
        val tvFollowing:TextView = findViewById(R.id.tv_following)
        val tvRepo:TextView = findViewById(R.id.tv_repo)

        val getData = intent.getParcelableExtra<Profile>(PARCEL)

        tvNama.text = getData?.name
        tvUsername.text = getData?.username
        tvCompany.text = getData?.company
        tvLocation.text = getData?.location
        tvFollowers.text = getData?.followers
        tvFollowing.text = getData?.following
        tvRepo.text = getData?.repository
        Glide.with(this)
            .load(getData?.avatar)
            .into(imgFoto)



    }
}