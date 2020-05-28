/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.mygithub.repos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mygithub.databinding.RepoItemLayoutBinding
import com.example.mygithub.network.GithubRepo

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClick a lambda that takes the
 */
class GithubRepoAdapter( val onClickListener: OnClickListener ) :
    ListAdapter<GithubRepo, GithubRepoAdapter.RepoViewHolder>(DiffCallback) {

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RepoViewHolder {
        return RepoViewHolder.from(parent)
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holderRepo: RepoViewHolder, position: Int) {
        val githubRepo = getItem(position)
        holderRepo.itemView.setOnClickListener {
            onClickListener.onClick(githubRepo)
        }
        holderRepo.bind(githubRepo)
    }

    /**
     * The RepoViewHolder constructor takes the binding variable from the associated
     * RepoItemLayout, which nicely gives it access to the full [GithubRepo] information.
     */
    class RepoViewHolder(private var binding: RepoItemLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(githubRepo: GithubRepo) {
            binding.githubRepo = githubRepo
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : RepoViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RepoItemLayoutBinding.inflate(layoutInflater, parent, false)

                return RepoViewHolder(binding)
            }
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MarsProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<GithubRepo>() {
        override fun areItemsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [MarsProperty]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [MarsProperty]
     */
    class OnClickListener(val clickListener: (githubRepo: GithubRepo) -> Unit) {
        fun onClick(githubRepo: GithubRepo) = clickListener(githubRepo)
    }
}
