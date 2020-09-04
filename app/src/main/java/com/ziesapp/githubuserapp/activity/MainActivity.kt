package com.ziesapp.githubuserapp.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.ziesapp.githubuserapp.R
import com.ziesapp.githubuserapp.adapter.ProfileAdapter
import com.ziesapp.githubuserapp.model.User
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ProfileAdapter
    private lateinit var listUser: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listUser = ArrayList()
        adapter = ProfileAdapter()
        adapter.notifyDataSetChanged()

        setAdapter()

    }

    private fun setAdapter() {
        recyclerView_main.layoutManager = LinearLayoutManager(this)
        recyclerView_main.setHasFixedSize(true)
        recyclerView_main.addItemDecoration(
            DividerItemDecoration(recyclerView_main.context, DividerItemDecoration.VERTICAL)
        )
        recyclerView_main.adapter = adapter

        adapter.setOnItemCallback(object : ProfileAdapter.OnItemCallBack {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        User(
            user.id,
            user.username,
            user.name,
            user.avatar,
            user.company,
            user.location,
            user.bio,
            user.web,
            user.repo
        )
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.PARCEL, user)
        startActivity(intent)
        Toast.makeText(this, "Kamu memilih ${user.username}", Toast.LENGTH_SHORT).show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                listUser.clear()
                getDataUserFromApi(query)
                showProgressBar(true)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_language_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.favorite -> {
                val mIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(mIntent)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun getDataUserFromApi(username: String?) {
        showProgressBar(true)
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
        client.addHeader("Authorization", "token aaa8a27fa7d182a6b48ac9d79c58c82a54766f82")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                showProgressBar(false)
                val result = String(responseBody!!)
                Log.d("MainActivity", result)
                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        getDetailUserFromApi(username)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("MainActivity", error?.message.toString())
            }
        })
    }

    fun getDetailUserFromApi(username: String?): ArrayList<User> {
        showProgressBar(true)
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("Authorization", "token aaa8a27fa7d182a6b48ac9d79c58c82a54766f82")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                showProgressBar(false)

                val result = String(responseBody!!)
                Log.d("MainActivity", result)
                try {
                    val responseObject = JSONObject(result)
                    val id = responseObject.getInt("id")
                    val username = responseObject.getString("login").toString()
                    val avatar = responseObject.getString("avatar_url").toString()
                    val name = responseObject.getString("name").toString()
                    val company = responseObject.getString("company").toString()
                    val repo = responseObject.getString("public_repos")
                    val location = responseObject.getString("location").toString()
                    val bio = responseObject.getString("bio").toString()
                    val web = responseObject.getString("blog").toString()
                    listUser.add(
                        User(
                            id, username, name, avatar, company, location, bio, web, repo
                        )
                    )
                    adapter.setData(listUser)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("MainActivity", error?.message.toString())
            }
        })
        return listUser
    }

    private fun showProgressBar(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
            logo.visibility = View.VISIBLE
            recyclerView_main.visibility = View.INVISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            logo.visibility = View.INVISIBLE
            recyclerView_main.visibility = View.VISIBLE
        }
    }
}