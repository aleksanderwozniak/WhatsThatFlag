package me.wozappz.whatsthatflag.utils

import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

/**
 * Created by olq on 21.11.17.
 */

fun ImageView.loadUrl(url: String, callback: Callback) {
    val picasso = Picasso.with(context)

    // for debugging
//    picasso.setIndicatorsEnabled(true)

    picasso.load(url)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .noPlaceholder()
            .into(this, callback)
}