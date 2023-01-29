package com.fanyfernaldi.pitjarustest.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fanyfernaldi.pitjarustest.RetroInstance
import com.fanyfernaldi.pitjarustest.responses.LoginResponse
import com.fanyfernaldi.pitjarustest.services.AuthService
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private lateinit var loginLiveData: MutableLiveData<LoginResponse?>

    init {
        loginLiveData = MutableLiveData()
    }

    fun getLoginObserver(): MutableLiveData<LoginResponse?> {
        return loginLiveData
    }

    fun login(request: RequestBody) {
        val retroInstance = RetroInstance.getRetroInstance().create(AuthService::class.java)
        val call = retroInstance.login(request)
        call.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.isSuccessful) {
                    loginLiveData.postValue(response.body())
                } else {
                    loginLiveData.postValue(null)
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                loginLiveData.postValue(null)
            }

        })
    }

}