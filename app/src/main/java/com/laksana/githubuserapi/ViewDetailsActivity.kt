package com.laksana.githubuserapi

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.laksana.githubuserapi.FollowingFragment.Companion.ARG_PARAM1
import com.laksana.githubuserapi.adapter.SectionsPagerAdapter
import com.laksana.githubuserapi.model.User
import com.laksana.githubuserapi.viewModel.DetailViewModel
import kotlinx.android.synthetic.main.details_user.*

class ViewDetailsActivity: AppCompatActivity() {
    companion object {
        const val NAME_FOLLOWER = "username"
    }

    private lateinit var detailViewModel : DetailViewModel

    private lateinit var viewPager2: ViewPager2
    private lateinit var  pagerAdapter: FragmentStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_user)

        val actionBar: ActionBar = supportActionBar!!
        actionBar.title = "Detail User"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val detailsUsername: TextView = findViewById(R.id.details_username)
        val location: TextView = findViewById(R.id.tvLocation)
        val repos: TextView = findViewById(R.id.tvTotalRepos)
        val totalFollowers: TextView = findViewById(R.id.tvTotalFollowers)
        val totalFollowing: TextView = findViewById(R.id.tvTotalFollowing)

        val data = intent.getParcelableExtra(NAME_FOLLOWER) as User
        val dataUser = data.username

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailViewModel::class.java)
        data.username.let { detailViewModel.showDetailsUser(it) }
        detailViewModel.getDetailUser().observe(this, {
            if (it != null) {
                Glide.with(this).load(data.avatar).into(details_img_avatar)
                detailsUsername.text = data.username
                location.text = it.location
                repos.text = it.repository
                totalFollowers.text = it.followers
                totalFollowing.text = it.following
            }
        })

        viewPager2 = findViewById(R.id.viewpager)
        pagerAdapter = SectionsPagerAdapter(this, dataUser)
        viewPager2.adapter = pagerAdapter

        TabLayoutMediator(tabs, viewPager2) { tab, position ->
            tab.text = when(position) {
                0-> resources.getString(R.string.tab_followers)
                else -> resources.getString(R.string.tab_following)
            }
        }.attach()

        showFollowers(dataUser)
        showFollowing(dataUser)
    }

    private fun showFollowers(dataUser: String){
        val fragmentFollowers: FollowersFragment = FollowersFragment.newInstance (dataUser)
        val bundle = Bundle()
        bundle.putString(FollowersFragment.NAME_FOLLOWER, dataUser)
        fragmentFollowers.arguments = bundle
    }

    private fun showFollowing(dataUser: String){
        val bundle = Bundle()
        val fragmentFollowing: FollowingFragment = FollowingFragment.newInstance(dataUser)
        bundle.putString(ARG_PARAM1, dataUser)
        fragmentFollowing.arguments = bundle
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}