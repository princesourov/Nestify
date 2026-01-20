package com.MSInnovation.nestify.core

import android.widget.EditText

fun EditText.extract(): String {
    return text.toString().trim()
}


