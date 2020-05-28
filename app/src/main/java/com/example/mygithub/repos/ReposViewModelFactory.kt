package com.example.mygithub.repos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ReposViewModelFactory(private val username: String) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReposViewModel::class.java)) {
            return ReposViewModel(username) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}