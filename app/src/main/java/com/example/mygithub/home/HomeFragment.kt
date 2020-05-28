package com.example.mygithub.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

import com.example.mygithub.databinding.HomeFragmentBinding
import com.example.mygithub.network.GithubApiStatus

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.viewModel = viewModel

        binding.reposButton.setOnClickListener {
            viewModel.actionViewRepos()
        }

        viewModel.viewRepos.observe(viewLifecycleOwner, Observer {
            if (it) {
                requireView().findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToReposFragment(viewModel.username.value ?: ""))
                viewModel.viewReposComplete()
            }
        })

        viewModel.githubUser.observe(viewLifecycleOwner, Observer {
            binding.executePendingBindings()
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            if (it == GithubApiStatus.ERROR){
                Toast.makeText(this.context, "Network error or User ${viewModel.userText.value} not found", Toast.LENGTH_SHORT).show()
                viewModel.startStatus()
            }
        })
    }

}
