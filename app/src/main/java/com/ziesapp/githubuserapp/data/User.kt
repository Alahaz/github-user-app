package com.ziesapp.githubuserapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    var username: String? = null,
    var avatar: String? = null,
    var name: String? = null,
    var location: String? = null,
    var bio: String? = null,
    var company: String? = null,
    var web: String? = null,
    var repo: String? = null
) : Parcelable