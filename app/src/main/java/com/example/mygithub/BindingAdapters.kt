package com.example.mygithub

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mygithub.network.GithubApiStatus
import com.example.mygithub.network.GithubRepo
import com.example.mygithub.repos.GithubRepoAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<GithubRepo>?) {
    val adapter = recyclerView.adapter as GithubRepoAdapter
    Log.i("BindRecyclerView", data.toString())
    adapter.submitList(data)
}

@BindingAdapter("userStatus")
fun userLoaded(view: ConstraintLayout, githubApiStatus: GithubApiStatus) {
    val visibility = when (githubApiStatus) {
        GithubApiStatus.LOADING -> View.VISIBLE
        GithubApiStatus.DONE -> View.VISIBLE
        else -> View.INVISIBLE
    }
    view.getViewById(R.id.user_image).visibility = visibility
    view.getViewById(R.id.user_text).visibility = visibility
    view.getViewById(R.id.repos_button).visibility = visibility
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)
        imgView.visibility = View.VISIBLE
    }
}

@BindingAdapter("githubApiStatus")
fun bindStatus(statusImageView: ImageView, status: GithubApiStatus?) {
    when (status) {
        GithubApiStatus.START -> {
            statusImageView.visibility = View.GONE
        }
        GithubApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        GithubApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        GithubApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}
