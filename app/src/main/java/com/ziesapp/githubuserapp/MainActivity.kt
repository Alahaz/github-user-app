package com.ziesapp.githubuserapp

import ProfileAdapter
import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    private lateinit var arrayUsername: Array<String>
    private lateinit var arrayLocation: Array<String>
    private lateinit var arrayRepository: Array<String>
    private lateinit var arrayFollower: Array<String>
    private lateinit var arrayFollowing: Array<String>
    private lateinit var arrayCompany: Array<String>
    private lateinit var arrayName: Array<String>
    private lateinit var arrayPhoto: TypedArray

    private lateinit var profileAdapter: ProfileAdapter

    private var listProfile = arrayListOf<Profile>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView: ListView = findViewById(R.id.lv_profile)
        profileAdapter = ProfileAdapter(this)
        listView.adapter = profileAdapter

        prepare()
        addItem()

//        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//            val intent = Intent(this@MainActivity,)
//        }

    }

    private fun addItem() {
        for (position in arrayName.indices) {
            val profile = Profile(
                arrayUsername[position],
                arrayName[position],
                arrayLocation[position],
                arrayRepository[position],
                arrayCompany[position],
                arrayFollower[position],
                arrayFollowing[position],
                arrayPhoto.getResourceId(position, -1)
            )
            listProfile.add(profile)
        }
        profileAdapter.profiles = listProfile
    }

    private fun prepare() {
        arrayName = resources.getStringArray(R.array.name)
        arrayCompany = resources.getStringArray(R.array.company)
        arrayFollower = resources.getStringArray(R.array.followers)
        arrayFollowing = resources.getStringArray(R.array.following)
        arrayLocation = resources.getStringArray(R.array.location)
        arrayUsername = resources.getStringArray(R.array.username)
        arrayRepository = resources.getStringArray(R.array.repository)
        arrayPhoto = resources.obtainTypedArray(R.array.avatar)
    }
}