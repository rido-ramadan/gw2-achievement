package com.edgardrake.gw2.achievement.utilities

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
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