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
import com.ziesapp.githubuserapp.R
import com.ziesapp.githubuserapp.adapter.ProfileAdapter
import com.ziesapp.githubuserapp.model.User
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray

class FollowingFragment : Fragment() {

    private lateinit var adapter: ProfileAdapter
    private lateinit var listFollowing: ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    companion object {
        const val ARG_NAME = "name"

        fun newInstance(username: String?): FollowingFragment {
            val fragment = FollowingFragment()
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
        listFollowing = ArrayList()
        listFollowing.clear()
        val username = arguments?.getString(ARG_NAME)
        getFollowingUserFromApi(username)
        rv_following.layoutManager = LinearLayoutManager(activity)
        rv_following.setHasFixedSize(true)
        rv_following.addItemDecoration(
            DividerItemDecoration(rv_following.context, DividerItemDecoration.VERTICAL)
        )
        rv_following.adapter = adapter
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getFollowingUserFromApi(username: String?) {
        showProgressBar(true)
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
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

                        listFollowing.add(user)
                    }
                    adapter.setData(listFollowing)
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
            rv_following.visibility = View.INVISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            rv_following.visibility = View.VISIBLE
        }
    }
}