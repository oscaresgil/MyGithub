package com.example.mygithub.repos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.mygithub.databinding.ReposFragmentBinding

class ReposFragment : Fragment() {

    companion object {
        fun newInstance() = ReposFragment()
    }

    private lateinit var viewModelFactory: ReposViewModelFactory
    private lateinit var viewModel: ReposViewModel

    private lateinit var binding: ReposFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ReposFragmentBinding.inflate(inflater)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this

        val args by navArgs<ReposFragmentArgs>()
        viewModelFactory = ReposViewModelFactory(args.username)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ReposViewModel::class.java)

        binding.viewModel = viewModel

        binding.reposList.adapter = GithubRepoAdapter(GithubRepoAdapter.OnClickListener {
            viewModel.openRepoUrl(it)
        })

        viewModel.currentGithubRepo.observe(viewLifecycleOwner, Observer {
            Log.i("ReposFragment", it.toString())
            openGithubPage(it.html_url)
        })
    }

    private fun openGithubPage(url: String) {
        val page: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, page)
        val packageManager = activity?.packageManager
        if (packageManager?.let { intent.resolveActivity(it) } != null) {
            startActivity(intent)
        }
    }
}
