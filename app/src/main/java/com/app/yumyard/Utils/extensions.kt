package com.app.yumyard.Utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

fun Context.shortToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.navigateTo(activity: Activity, value: Bundle?){
    val intent = Intent(this, activity::class.java)
    if (value!= null){
        intent.putExtras(value)
    }
    startActivity(intent)
}

fun Context.transitTo(fragment : Fragment, value: Bundle?){
    val fragmentManager = (this as AppCompatActivity).supportFragmentManager
    val fr: FragmentTransaction = fragmentManager.beginTransaction()
    if(value!=null){
        fragment.arguments = value
    }
//    fr.replace(R.id.frameLayout, fragment)
    fr.addToBackStack(null)
    fr.commit()
}

fun Context.updateSharedPreferences(isLogedIn : Boolean, isProfileCreated: Boolean, uId:String?){
    val editor = this.getSharedPreferences("SoulPath", Context.MODE_PRIVATE).edit()
    if (uId!=null) {
        editor.putString("uId", uId)
    }
    editor.putBoolean("IsLogedin", isLogedIn)
    editor.putBoolean("IsProfileCreated", isProfileCreated)
    editor.commit()
}



fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

