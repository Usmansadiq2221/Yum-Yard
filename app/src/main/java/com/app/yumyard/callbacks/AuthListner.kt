package com.app.yumyard.callbacks

interface AuthListner {

    fun onStarted()
    fun onSuccess()
    fun onFailure(message : String)
}