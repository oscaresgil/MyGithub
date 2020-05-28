package com.example.mygithub.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mygithub.network.GithubApi
import com.example.mygithub.network.GithubApiStatus
import com.example.mygithub.network.GithubRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ReposViewModel (private val username: String) : ViewModel() {

    private val _githubRepos = MutableLiveData<List<GithubRepo>>()
    val githubRepos: LiveData<List<GithubRepo>>
        get() = _githubRepos

    private val _status = MutableLiveData<GithubApiStatus>()
    val status: LiveData<GithubApiStatus>
        get() = _status

    private val _currentGithubRepo = MutableLiveData<GithubRepo>()
    val currentGithubRepo: LiveData<GithubRepo>
        get() = _currentGithubRepo

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        startStatus()
        getRepos()
    }

    fun startStatus(){
        _status.value = GithubApiStatus.START
    }

    fun openRepoUrl(githubRepo: GithubRepo){
        _currentGithubRepo.value = githubRepo
    }

    private fun getRepos(){
        coroutineScope.launch {
            val reposDeferred = GithubApi.retrofitService.getReposAsync(username)
            try {
                _status.value = GithubApiStatus.LOADING
                val repos = reposDeferred.await()
                _status.value = GithubApiStatus.DONE
                _githubRepos.value = repos
            } catch (e: Exception){
                _status.value = GithubApiStatus.ERROR
                _githubRepos.value = emptyList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
