package com.ziesapp.githubuserapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.ziesapp.githubuserapp.db.DatabaseContract.AUTHORITY
import com.ziesapp.githubuserapp.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.ziesapp.githubuserapp.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.ziesapp.githubuserapp.db.UserHelper

class GithubUserProvider : ContentProvider() {

    companion object {
        private const val USER = 1
        private const val USER_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userHelper: UserHelper

        /*
       Uri matcher untuk mempermudah identifier dengan menggunakan integer
       misal
       uri com.dicoding.picodiploma.mynotesapp dicocokan dengan integer 1
       uri com.dicoding.picodiploma.mynotesapp/# dicocokan dengan integer 2
        */

        init {
            // content://com.ziesapp.githubuserapp/note
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)

            // content://com.ziesapp.githubuserapp/note/id
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", USER_ID)
        }
    }

    override fun onCreate(): Boolean {
        userHelper = UserHelper(context as Context)
        userHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            USER -> cursor = userHelper.queryAll()
            USER_ID -> cursor = userHelper.queryById(uri.lastPathSegment.toString())
            else -> cursor = null
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> userHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> userHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> userHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }
}
