package com.laksana.githubuserapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.laksana.githubuserapi.adapter.FollowersAdapter
import com.laksana.githubuserapi.model.User
import com.laksana.githubuserapi.viewModel.FollowersViewModel
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment : Fragment() {
    companion object {
        const val NAME_FOLLOWER = "name_follower"

        fun newInstance(username: String): FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(NAME_FOLLOWER, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var adapter: FollowersAdapter
    private var listData = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowersAdapter(listData)
        adapter.notifyDataSetChanged()

        recyclerView_followers.adapter = adapter
        recyclerView_followers.setHasFixedSize(true)
        recyclerView_followers.layoutManager = LinearLayoutManager(context)

        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowersViewModel::class.java)

        if(arguments != null) {
            val username = arguments?.getString(NAME_FOLLOWER)
            showProgressBar(true)
            followersViewModel.setFollowers(username.toString())
        }

        followersViewModel.getFollowers()
            .observe(viewLifecycleOwner, {
                adapter.setData(it)

                showProgressBar(false)
            })
    }

    private fun showProgressBar(state: Boolean) {
        if(state) {
            progressBarFollowers.visibility = View.VISIBLE
        } else {
            progressBarFollowers.visibility = View.GONE
        }
    }

}

