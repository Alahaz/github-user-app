package com.ziesapp.githubuserapp.helper

import android.database.Cursor
import com.ziesapp.githubuserapp.db.DatabaseContract
import com.ziesapp.githubuserapp.model.User

object MappingHelper {
    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User> {
        val listUser = ArrayList<User>()

        userCursor?.apply {
            while (moveToNext()) {
                val user = User()
                user.id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                user.username =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_USERNAME))
                user.avatar =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_AVATAR_URL))
                listUser.add(user)
            }
        }
        return listUser
    }

    fun mapCursorToObject(userCursor: Cursor?): User {
        var user = User()
        userCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
            val username =
                getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_USERNAME))
            val avatar =
                getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_AVATAR_URL))
            user = User(id, username, avatar)
        }
        return user
    }

}