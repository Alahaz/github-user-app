package com.ziesapp.githubuserapp.activity

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ziesapp.githubuserapp.R
import com.ziesapp.githubuserapp.adapter.PagerAdapter
import com.ziesapp.githubuserapp.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import com.ziesapp.githubuserapp.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import com.ziesapp.githubuserapp.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.ziesapp.githubuserapp.db.DatabaseContract.UserColumns.Companion._ID
import com.ziesapp.githubuserapp.db.UserHelper
import com.ziesapp.githubuserapp.helper.MappingHelper
import com.ziesapp.githubuserapp.model.User
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private var statusFavorite = false
    private var getData: User? = null
    private var position: Int = 0

    private lateinit var uriWithId: Uri
    private lateinit var userHelper: UserHelper

    companion object {
        const val PARCEL = "Parcel"
        const val EXTRA_USER = "extra_user"
        const val EXTRA_POSITIION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELTE = 20
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setupActionBar()


        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        getData = intent.getParcelableExtra(PARCEL)
        setUser()

        fab_fav.setOnClickListener(this)

//        persiapan untuk content resolver
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + getData?.id)
        val cursorUri = contentResolver?.query(uriWithId, null, null, null, null)
        isAlreadyFavorite(cursorUri)


    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detailed User"
    }

    private fun setUser() {
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
        setupPager()
    }

    private fun setupPager() {
        val pagerAdapter = PagerAdapter(
            this,
            supportFragmentManager
        )
        pagerAdapter.username = getData?.username
        view_pager.adapter = pagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }


    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            fab_fav.setImageResource(R.drawable.fav_solid)
        } else {
            fab_fav.setImageResource(R.drawable.fav_border)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_fav -> setFavorite()
        }
    }

    private fun isAlreadyFavorite(cursorUri: Cursor?) {
        val userFav = MappingHelper.mapCursorToArrayList(cursorUri)
        for (data in userFav) {
            if (this.getData?.id == data.id) {
                statusFavorite = true
            }
        }
        setStatusFavorite(statusFavorite)
    }

    private fun setFavorite() {
        if (statusFavorite) {
            contentResolver.delete(uriWithId, null, null)
//            userHelper.deleteById(getData?.id.toString()).toLong()
//                .delete(uriWithId, null, null)
            Toast.makeText(this, "${getData?.name} Unfavorited", Toast.LENGTH_SHORT).show()
            statusFavorite = false
            setStatusFavorite(statusFavorite)
        } else {
            val intent = Intent()
            intent.putExtra(EXTRA_USER, getData)

            val values = ContentValues()
            values.put(_ID, getData?.id)
            values.put(COLUMN_NAME_USERNAME, getData?.username)
            values.put(COLUMN_NAME_AVATAR_URL, getData?.avatar)
            contentResolver.insert(CONTENT_URI, values)
//            val result = userHelper.insert(values)
//            setResult(RESULT_ADD, intent)
//            contentResolver.insert(CONTENT_URI, values)
            Toast.makeText(this, "${getData?.name} Favorited", Toast.LENGTH_SHORT).show()
            statusFavorite = true
            setStatusFavorite(statusFavorite)
        }
    }
}