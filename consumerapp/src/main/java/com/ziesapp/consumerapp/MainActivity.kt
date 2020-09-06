package com.ziesapp.consumerapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziesapp.consumerapp.adapter.ConsumerAdapter
import com.ziesapp.consumerapp.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.ziesapp.consumerapp.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ConsumerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "List Favorite Github User"
        setupAdapter()
        startAsyncJob()
    }

    private fun setupAdapter() {
        adapter = ConsumerAdapter(this)
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
        rv_favorite.adapter = adapter
        rv_favorite.addItemDecoration(
            DividerItemDecoration(
                rv_favorite.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun startAsyncJob() {
        GlobalScope.launch(Dispatchers.Main) {
            val defferedUser = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val fav = defferedUser.await()
            if (fav.size > 0) {
                adapter.listFav = fav
            } else {
                adapter.listFav = ArrayList()
                Toast.makeText(this@MainActivity, "Favorite User Empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}