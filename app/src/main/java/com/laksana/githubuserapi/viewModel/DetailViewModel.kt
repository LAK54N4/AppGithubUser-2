package com.laksana.githubuserapi.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laksana.githubuserapi.model.UserDetail
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel: ViewModel() {
    private val detailUser = MutableLiveData<UserDetail>()

    fun showDetailsUser(username: String) {

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"

        client.addHeader("Authorization","token 1590d4705bf0e9599411df2fd69c161057c6c5c3")
        client.addHeader("User-Agent","request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    Log.d("detailUser", responseObject.toString())

                    val userDetail = responseObject.getString("login")
                    val location = responseObject.getString("location")
                    val repository = responseObject.getString("public_repos")
                    val followers = responseObject.getString("followers")
                    val following = responseObject.getString("following")
                    val avatar = responseObject.getString("avatar_url")

                    val user = UserDetail (userDetail, location, repository, followers, following, avatar)
                    detailUser.postValue(user)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getDetailUser(): LiveData<UserDetail> {
        return detailUser
    }

}