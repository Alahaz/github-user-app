package com.ziesapp.githubuserapp.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziesapp.githubuserapp.R
import com.ziesapp.githubuserapp.adapter.FavoriteAdapter
import com.ziesapp.githubuserapp.db.UserHelper
import com.ziesapp.githubuserapp.helper.MappingHelper
import com.ziesapp.githubuserapp.model.User
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter
    private lateinit var userHelper: UserHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        setupActionBar()

        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        rv_favorite.adapter = adapter

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()


        if (savedInstanceState == null) {
            loadUserAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) {
                adapter.listFav = list
            }
        }
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val defferedUser = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
//                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val fav = defferedUser.await()
            if (fav.size > 0) {
                adapter.listFav = fav
            } else {
                adapter.listFav = ArrayList()
                Toast.makeText(this@FavoriteActivity, "Favorite User Empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }
//        showRecylerView()
    }

//    private fun showRecylerView() {
//        rv_favorite.adapter = adapter
//        rv_favorite.layoutManager = LinearLayoutManager(this)
//        rv_favorite.setHasFixedSize(true)
//    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favorited User"
    }
}