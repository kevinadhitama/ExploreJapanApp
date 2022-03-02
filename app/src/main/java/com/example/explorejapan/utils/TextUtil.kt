package com.example.explorejapan.utils

import android.os.Build
import android.text.Html
import android.text.Spanned

class TextUtil {
    companion object {

        fun fromHtml(text: String): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(text)
        }
    }
}