package com.olq.whatsthatflag.data

import android.content.Context
import com.olq.whatsthatflag.R
import java.io.BufferedReader
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

class Downloader (private val ctx: Context) {


    fun loadAllFlagsFromRes(): List<String> {
        return ctx.resources.getStringArray(R.array.list_of_countries).toList()
    }

    fun loadContinentSpliteratorFromRes(): IntArray {
        return ctx.resources.getIntArray(R.array.continent_spliterator)
    }


    fun getWikipediaLink(validCountryName: String): String {
        return ctx.getString(R.string.link_wikipedia, validCountryName)
    }


    fun getImgURL(urlString: String): String? {
        var inputStream: InputStream? = null
        var imgURL: String ?= null

        try {
            inputStream = setupConnectionStream(urlString)
            imgURL = extractImgURL(inputStream)

        } catch (e: Exception) {
            e.printStackTrace()

        } finally {
            if (inputStream != null) {
                inputStream.close()
            }
        }

        return imgURL
    }

    private fun setupConnectionStream(countriesURL: String) : InputStream{
        val url = URL(countriesURL)
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.connect()

        return connection.inputStream
    }

    private fun extractImgURL(inputStream: InputStream): String? {
        val reader: BufferedReader = inputStream.bufferedReader()
        var dataLine: String? = reader.readLine()

        while (dataLine != null && !dataLine.contains("og:image")) {
            dataLine = reader.readLine()
        }

        if (dataLine != null) {
            val content = dataLine

            val p = Pattern.compile("\"og:image\" content=\"(.*?)\"")
            val m = p.matcher(content)

            if (m.find()) {
                return m.group(1)
            }
        }

        return null
    }
}