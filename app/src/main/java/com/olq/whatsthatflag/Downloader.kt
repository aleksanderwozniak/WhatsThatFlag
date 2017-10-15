package com.olq.whatsthatflag

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.BufferedReader
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

class Downloader(imgView: ImageView, progressBar: ProgressBar) {

    val myImgView = imgView
    val myProgressBar = progressBar


    fun downloadListOfCountries(list: MutableList<String>){
        val countriesURL = "https://www.state.gov/misc/list/"
        var inputStream: InputStream? = null

        try {
            val url = URL(countriesURL)
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.connect()

            inputStream = connection.inputStream


            val reader: BufferedReader = inputStream.bufferedReader()
            var dataLine: String? = reader.readLine()


            //MARK: START of HTML section containing names of countries
            while (dataLine != null && !dataLine.contains("<!--Responsive Alphabetical Nav Targets-->")) {
                dataLine = reader.readLine()
            }


            //MARK: END of that HTML section
            while (dataLine != null && !dataLine.contains("<!--/Responsive Alphabetical Nav Targets-->")){
                dataLine = reader.readLine()

                val p = Pattern.compile("\">(.*?)</a></li>")
                val m = p.matcher(dataLine)

                if (m.find()) {
                    Log.d("Regex", m.group(1).toString())
                    list.add(m.group(1))
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()

        } finally {
            if (inputStream != null) {
                inputStream.close()
            }
        }
    }



    fun downloadImage(urlString: String) {
        showProgressBar()

        doAsync {
            val currentURL: String ?= getImgURL(urlString)

            uiThread {
                Log.d("Download Image", "Finished")
                hideProgressBar()

                if(currentURL != null) {
                    Log.d("Download Image", "CurrentURL: $currentURL")
                    myImgView.loadUrl(currentURL)
                }
            }
        }
    }

    fun getImgURL(urlString: String): String? {
        var inputStream: InputStream? = null
        var imgURL: String ?= null

        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.connect()

            inputStream = connection.inputStream

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

    fun extractImgURL(inputStream: InputStream): String? {
        val reader: BufferedReader = inputStream.bufferedReader()
        var dataLine: String? = reader.readLine()
        val content: String

        while (dataLine != null && !dataLine.contains("og:image")) {
            dataLine = reader.readLine()
        }

        if (dataLine != null) {
            content = dataLine

            val p = Pattern.compile("\"og:image\" content=\"(.*?)\"")
            val m = p.matcher(content)

            if (m.find()) {
                return m.group(1)
            }
        }

        return null
    }


    fun showProgressBar() {
        myProgressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        myProgressBar.visibility = View.INVISIBLE
    }


    fun ImageView.loadUrl(url: String) {
        Picasso.with(context).load(url).into(this)
    }
}