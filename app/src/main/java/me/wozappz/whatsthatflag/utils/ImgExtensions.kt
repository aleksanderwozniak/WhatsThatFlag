package me.wozappz.whatsthatflag.utils

import android.graphics.Bitmap
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

/**
 * Created by olq on 21.11.17.
 */

fun ImageView.loadUrl(url: String, offline: Boolean = true, callback: Callback) {
    val picasso = Picasso.with(context)

    val netPolicy = if (offline) NetworkPolicy.OFFLINE else NetworkPolicy.NO_CACHE

    picasso.load(url)
            .config(Bitmap.Config.RGB_565)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .networkPolicy(netPolicy)
            .noPlaceholder()
            .into(this, callback)
}