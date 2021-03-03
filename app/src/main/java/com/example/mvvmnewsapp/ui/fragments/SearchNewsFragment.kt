package com.example.mvvmnewsapp.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.adapters.NewsAdapter
import com.example.mvvmnewsapp.databinding.FragmentBreakingNewsBinding
import com.example.mvvmnewsapp.databinding.FragmentSearchNewsBinding
import com.example.mvvmnewsapp.ui.NewsActivity
import com.example.mvvmnewsapp.ui.NewsViewModel
import com.example.mvvmnewsapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {

    private lateinit var binding: FragmentSearchNewsBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchNewsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel
        setUpRecyclerView()

        var job: Job? = null

        binding.etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                binding.etSearch?.let {
                    if (binding.etSearch.text.toString().isNotEmpty()) {
                        viewModel.searchNews(binding.etSearch.text.toString())
                    }
                }
            }
        }

        viewModel.searchNews_MLD.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoadingAnimation()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    response.massage?.let {
                        Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showLoadingAnimation()
                }
            }
        })
    }

    private fun hideLoadingAnimation() {
        binding.animationView.visibility = View.INVISIBLE
    }

    private fun showLoadingAnimation() {
        binding.animationView.visibility = View.VISIBLE
    }


    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


}