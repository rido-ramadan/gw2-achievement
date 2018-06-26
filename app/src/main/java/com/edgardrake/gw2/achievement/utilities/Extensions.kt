package com.edgardrake.gw2.achievement.utilities

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.onChange(callback: (String) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            callback(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}