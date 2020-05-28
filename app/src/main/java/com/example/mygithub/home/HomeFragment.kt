package com.example.mygithub.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

import com.example.mygithub.databinding.HomeFragmentBinding

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
                viewModel.githubUser.value?.let {user ->
                    requireView().findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToReposFragment(user.login))
                    viewModel.viewReposComplete()
                }
            }
        })
        viewModel.githubUser.observe(viewLifecycleOwner, Observer {
            Log.i("HomeFragment", it.toString())
            binding.executePendingBindings()
        })
    }

}
