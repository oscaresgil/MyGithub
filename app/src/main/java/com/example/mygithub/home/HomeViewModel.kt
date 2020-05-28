package com.example.mygithub.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.mygithub.network.GithubApi
import com.example.mygithub.network.GithubApiStatus
import com.example.mygithub.network.GithubUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _status = MutableLiveData<GithubApiStatus>()
    val status: LiveData<GithubApiStatus>
        get() = _status

    private val _viewRepos = MutableLiveData<Boolean>()
    val viewRepos: LiveData<Boolean>
        get() = _viewRepos

    val userText = MutableLiveData<String>()

    private val _githubUser = MutableLiveData<GithubUser>()
    val githubUser: LiveData<GithubUser>
        get() = _githubUser

//    val username = Transformations.map(githubUser) { it.login }

    init {
        _status.value = GithubApiStatus.START
    }

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun search(){
        coroutineScope.launch {
            val githubUserDeferred = GithubApi.retrofitService.getUserAsync(userText.value)
            try {
                _status.value = GithubApiStatus.LOADING
                Log.i("HomeViewModel", githubUser.toString())
                val githubUser = githubUserDeferred.await()
                _status.value = GithubApiStatus.DONE
                _githubUser.value = githubUser
            } catch (e: Exception) {
                _status.value = GithubApiStatus.ERROR
                Log.i("HomeViewModel", githubUser.toString())
            }

        }
    }

    fun actionViewRepos() {
        _viewRepos.value = true
    }

    fun viewReposComplete() {
        _viewRepos.value = false
    }
}
