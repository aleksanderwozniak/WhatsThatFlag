package me.wozappz.whatsthatflag.data.model

import android.content.Context
import android.graphics.Bitmap
import com.squareup.picasso.Picasso
import me.wozappz.whatsthatflag.R
import javax.inject.Inject

class DataLoader @Inject constructor(private val ctx: Context) {


    private fun loadAllFlagsFromRes(): Array<String> {
        return ctx.resources.getStringArray(R.array.list_of_countries)
    }

    fun loadContinentSpliteratorFromRes(): IntArray {
        return ctx.resources.getIntArray(R.array.continent_spliterator)
    }


    fun getWikipediaLink(validCountryName: String): String {
        return ctx.getString(R.string.link_wikipedia, validCountryName)
    }

    private fun loadFlagImgUrlsFromRes(): Array<String> {
        return ctx.resources.getStringArray(R.array.list_of_flagimg_urls)
    }

    fun getWtfFlagList(): List<Pair<String, String>> {
        return loadAllFlagsFromRes().zip(loadFlagImgUrlsFromRes())
    }

    fun fetchFlag(flagUrl: String) {
        Picasso.with(ctx)
                .load(flagUrl)
                .config(Bitmap.Config.RGB_565)
                .fetch()
    }
}