package com.olq.whatsthatflag.utils

import android.widget.ImageView
import com.squareup.picasso.Picasso

/**
 * Created by olq on 21.11.17.
 */

fun ImageView.loadUrl(url: String) {
    Picasso.with(context).load(url).into(this)
}