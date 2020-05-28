package com.example.mygithub.network

data class GithubRepo (
    val id: Int,
    val name: String,
    val description: String?,
    val html_url: String
)