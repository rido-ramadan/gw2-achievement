package com.edgardrake.gw2.achievement.utilities

import android.app.Activity
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

fun EditText.onChange(callback: (String) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            callback(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun <T> Observable<T>.httpCall(callback: ((T) -> Unit),
                               onError: ((Throwable) -> Unit) = {
                                   error: Throwable ->
                                    if (error is HttpException) {Log.e("HTTP-Error", "${error.code()}: ${error.message()}")}
                                    else {Log.e("Exception", "${error.message}")}}) {
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(callback, onError)
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    LayoutInflater.from(this.context).inflate(layoutRes, this, false)

fun GridLayoutManager.setLookupSize(columnSizeByPosition: (Int) -> Int) {
    this.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int) = columnSizeByPosition(position)
    }
}

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.toast(resID: Int) {
    Toast.makeText(this, resID, Toast.LENGTH_SHORT).show()
}

fun Activity.snackbar(text: String) {
    Snackbar.make(findViewById<ViewGroup>(android.R.id.content), text, Snackbar.LENGTH_SHORT)
        .setAction(android.R.string.ok, null)
        .show()
}

fun Fragment.toast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(resID: Int) {
    Toast.makeText(requireContext(), resID, Toast.LENGTH_SHORT).show()
}

fun Fragment.snackbar(text: String) {
    Snackbar.make(requireActivity().findViewById<ViewGroup>(android.R.id.content), text, Snackbar.LENGTH_SHORT)
        .setAction(android.R.string.ok, null)
        .show()
}