package com.ziesapp.githubuserapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.ziesapp.githubuserapp.adapter.ProfileAdapter
import com.ziesapp.githubuserapp.R
import com.ziesapp.githubuserapp.data.User
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_followers.*
import kotlinx.android.synthetic.main.fragment_followers.progressBar
import org.json.JSONArray

class FollowersFragment : Fragment() {

    private lateinit var adapter: ProfileAdapter
    private lateinit var listFollower: ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    companion object {
        const val ARG_NAME = "name"

        fun newInstance(username: String?): FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle().apply {
                putString(ARG_NAME, username)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ProfileAdapter()
        adapter.notifyDataSetChanged()
        listFollower = ArrayList()
        listFollower.clear()
        val username = arguments?.getString(ARG_NAME)
        getFollowerUserFromApi(username)
        rv_followers.layoutManager = LinearLayoutManager(activity)
        rv_followers.setHasFixedSize(true)
        rv_followers.addItemDecoration(
            DividerItemDecoration(rv_followers.context, DividerItemDecoration.VERTICAL)
        )
        rv_followers.adapter = adapter
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getFollowerUserFromApi(username: String?) {
        showProgressBar(true)
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/followers"
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
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val user = User()
                        user.username = jsonObject.getString("login")
                        user.avatar = jsonObject.getString("avatar_url")

                        listFollower.add(user)
                    }
                    adapter.setData(listFollower)
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
                TODO("Not yet implemented")
            }
        })

    }

    private fun showProgressBar(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
            rv_followers.visibility = View.INVISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            rv_followers.visibility = View.VISIBLE
        }
    }
}