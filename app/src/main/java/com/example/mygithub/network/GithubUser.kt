package com.example.mygithub.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GithubUser (
    val name: String,
    val login: String,
    @Json(name = "avatar_url")
    val avatarUrl: String
) : Parcelable