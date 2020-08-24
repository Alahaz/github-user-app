package com.ziesapp.githubuserapp.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ziesapp.githubuserapp.R
import com.ziesapp.githubuserapp.fragment.FollowersFragment
import com.ziesapp.githubuserapp.fragment.FollowingFragment

class PagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(
    fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    var username: String? = null

    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.tab_followers,
        R.string.tab_following
    )

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment.newInstance(username)
            1 -> fragment = FollowingFragment.newInstance(username)
        }
        return fragment as Fragment
    }

    override fun getCount(): Int = TAB_TITLES.size

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }
}