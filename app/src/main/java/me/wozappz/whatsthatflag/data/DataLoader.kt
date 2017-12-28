package me.wozappz.whatsthatflag.data

import android.content.Context
import me.wozappz.whatsthatflag.R

class DataLoader(private val ctx: Context) {


    fun loadAllFlagsFromRes(): Array<String> {
        return ctx.resources.getStringArray(R.array.list_of_countries)
    }

    fun loadContinentSpliteratorFromRes(): IntArray {
        return ctx.resources.getIntArray(R.array.continent_spliterator)
    }


    fun getWikipediaLink(validCountryName: String): String {
        return ctx.getString(R.string.link_wikipedia, validCountryName)
    }

    fun loadFlagImgUrlsFromRes(): Array<String> {
        return ctx.resources.getStringArray(R.array.list_of_flagimg_urls)
    }

    fun getWtfFlagList(): List<Pair<String, String>> {
        return loadAllFlagsFromRes().zip(loadFlagImgUrlsFromRes())
    }
}