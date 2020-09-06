package com.ziesapp.consumerapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    var id: Int? = 0,
    var username: String? = null,
    var name: String? = null,
    var avatar: String? = null,
    var company: String? = null,
    var location: String? = null,
    var bio: String? = null,
    var web: String? = null,
    var repo: String? = null
) : Parcelable