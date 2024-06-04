package com.app.yumyard.ViewModels

import android.view.View
import androidx.lifecycle.ViewModel
import com.app.yumyard.callbacks.AuthListner

class AuthViewModel : ViewModel() {

    var email: String? = null
    var password: String? = null

    var authListner : AuthListner? = null

    fun onLoginButtonClicked(view: View){
        authListner?.onStarted()
        if (email.isNullOrEmpty() || password.isNullOrEmpty()){
            authListner?.onFailure("Invalid Email or Password")

            return
        }


        //success...
        authListner?.onSuccess()
    }
}