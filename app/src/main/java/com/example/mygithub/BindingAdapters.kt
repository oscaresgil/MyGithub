package com.example.mygithub

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
    adapter.submitList(data)
}

@BindingAdapter("userStatus")
fun userLoaded(view: ConstraintLayout, githubApiStatus: GithubApiStatus) {
    val userImage = view.getViewById(R.id.user_image) as ImageView
    val userText = view.getViewById(R.id.user_text) as TextView
    val usernameText = view.getViewById(R.id.username_text) as TextView
    val reposButton = view.getViewById(R.id.repos_button) as Button
    when (githubApiStatus) {
        GithubApiStatus.START, GithubApiStatus.ERROR -> {
            view.visibility = View.INVISIBLE
        }
        GithubApiStatus.LOADING -> {
            view.visibility = View.VISIBLE
            userImage.setImageDrawable(null)
            userText.visibility = View.INVISIBLE
            usernameText.visibility = View.INVISIBLE
            reposButton.visibility = View.INVISIBLE
        }
        GithubApiStatus.DONE -> {
            userText.visibility = View.VISIBLE
            usernameText.visibility = View.VISIBLE
            reposButton.visibility = View.VISIBLE
        }
    }
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
