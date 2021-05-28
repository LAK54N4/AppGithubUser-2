package com.laksana.githubuserapi

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.laksana.githubuserapi.adapter.UserAdapter
import com.laksana.githubuserapi.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val listUser = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getDataApi()
    }

    private fun getDataApi() {
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"

        client.addHeader("Authorization","token ----------")
        client.addHeader("User-Agent","request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                progressBar.visibility = View.INVISIBLE

                val result = String(responseBody)

                try {
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")

                        val user = User(username, avatar)
                        user.username = username
                        user.avatar = avatar
                        listUser.add(user)

                        showAndClick()
                    }

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                progressBar.visibility = View.INVISIBLE

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getDataUserFromApi(username: String) {

        progressBar.visibility = View.VISIBLE

        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"

        client.addHeader("Authorization","token 1590d4705bf0e9599411df2fd69c161057c6c5c3")
        client.addHeader("User-Agent","request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                progressBar.visibility = View.INVISIBLE

                val result = String(responseBody)

                try {
                    val responseObject = JSONObject(result)
                    val itemsUser = responseObject.getJSONArray("items")
                    listUser.clear()
                    for (i in 0 until itemsUser.length()) {
                        val searchItems = itemsUser.getJSONObject(i)
                        val searchUsers = searchItems.getString("login")
                        val searchAvatar = searchItems.getString("avatar_url")

                        val searchUser = User(searchUsers, searchAvatar)
                        searchUser.username = searchUsers
                        searchUser.avatar = searchAvatar
                        listUser.add(searchUser)
                        showAndClick()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                getDataUserFromApi(query)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAndClick() {
        val adapter = UserAdapter(listUser)
        rv_listName.layoutManager = LinearLayoutManager(this@MainActivity)
        rv_listName.adapter = adapter

        adapter.setOnItemClickCallback(object : OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val moveViewDetail = Intent (this@MainActivity, ViewDetailsActivity::class.java)
                moveViewDetail.putExtra(ViewDetailsActivity.NAME_FOLLOWER, data)
                startActivity(moveViewDetail)
            }
        })
    }
}
