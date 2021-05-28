package com.laksana.githubuserapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.laksana.githubuserapi.adapter.FollowingAdapter
import com.laksana.githubuserapi.model.User
import com.laksana.githubuserapi.viewModel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(ARG_PARAM1)
        }
    }

    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var adapter: FollowingAdapter
    private var listData = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowingAdapter (listData)
        adapter.notifyDataSetChanged()

        recyclerView_following.adapter = adapter
        recyclerView_following.setHasFixedSize(true)
        recyclerView_following.layoutManager = LinearLayoutManager(context)

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)

        if (arguments != null) {
            val username = arguments?.getString(ARG_PARAM1)
            showProgressBar(true)
            followingViewModel.setFollowing(username.toString())
        }

        followingViewModel.getFollowing().observe(viewLifecycleOwner, {
            adapter.setData(it)
            showProgressBar(false)
        })
    }

    private fun showProgressBar(state: Boolean) {
        if(state) {
            progressBarFollowing.visibility = View.VISIBLE
        } else {
            progressBarFollowing.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_PARAM1 = "username"

        fun newInstance(username: String?) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, username)
                }
            }
    }
}