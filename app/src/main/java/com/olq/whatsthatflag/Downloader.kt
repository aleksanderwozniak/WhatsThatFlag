package com.olq.whatsthatflag

import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

object Downloader {

    fun downloadListOfCountries(list: MutableList<String>){
        val countriesURL = "https://simple.wikipedia.org/wiki/List_of_countries"
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
            while (dataLine != null && !dataLine.contains("title=\"Sovereign state\"")) {
                dataLine = reader.readLine()
            }


            //MARK: END of that HTML section
            while (dataLine != null && !dataLine.contains("Zimbabwe")){
                dataLine = reader.readLine()

                val p = Pattern.compile("title=\"(.*?)\">")
                val m = p.matcher(dataLine)

                while (m.find()) {
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






    fun downloadEuropeanCountries(list: MutableList<String>){
        val countriesURL = "https://simple.wikipedia.org/wiki/List_of_European_countries"
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
            while (dataLine != null && !dataLine.contains("/wiki/Capital_(political)")) {
                dataLine = reader.readLine()
            }


            //MARK: END of that HTML section
            while (dataLine != null && !dataLine.contains("</table>")){
                dataLine = reader.readLine()

                if(dataLine.contains("<span class")) {
                    val p = Pattern.compile("title=\"(.*?)\">")
                    val m = p.matcher(dataLine)

                    if (m.find()) {
                        Log.d("Regex", m.group(1).toString())
                        list.add(m.group(1))
                    }
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




    fun downloadAsianCountries(list: MutableList<String>){
        val countriesURL = "https://simple.wikipedia.org/wiki/Asia"
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
            while (dataLine != null && !dataLine.contains("id=\"List_of_Asian_Countries\"")) {
                dataLine = reader.readLine()
            }


            //MARK: END of that HTML section
            while (dataLine != null && !dataLine.contains("id=\"Related_pages\"")){
                dataLine = reader.readLine()

                val p = Pattern.compile("title=\"(.*?)\">")
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
}