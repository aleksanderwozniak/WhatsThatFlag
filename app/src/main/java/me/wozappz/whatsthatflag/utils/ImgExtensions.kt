package me.wozappz.whatsthatflag.utils

import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

/**
 * Created by olq on 21.11.17.
 */

fun ImageView.loadUrl(url: String, callback: Callback) {
    Picasso.with(context).load(url).into(this, callback)
}