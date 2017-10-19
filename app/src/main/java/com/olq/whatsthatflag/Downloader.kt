package com.olq.whatsthatflag

import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

object Downloader {

    val COUNTRIES_URL = "https://en.wikipedia.org/wiki/" +
            "List_of_sovereign_states_and_dependent_territories_by_continent"

    fun downloadGlobalList(list: MutableList<String>){
        val countriesURL = "https://simple.wikipedia.org/wiki/List_of_countries"
        var inputStream: InputStream? = null

        try {
            inputStream = setupConnectionStream(countriesURL)

            val reader: BufferedReader = inputStream.bufferedReader()
            var dataLine: String? = reader.readLine()


            //MARK: START of HTML section containing names of countries
            while (dataLine != null && !dataLine.contains("title=\"Sovereign state\"")) {
                dataLine = reader.readLine()
            }


            //MARK: END of that HTML section
            while (dataLine != null && !dataLine.contains("Zimbabwe")){
                dataLine = reader.readLine()

                setupRegex(list, dataLine, "title=\"(.*?)\">")
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
            inputStream = setupConnectionStream(countriesURL)

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
                    setupRegex(list, dataLine, "title=\"(.*?)\">")
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
            inputStream = setupConnectionStream(countriesURL)

            val reader: BufferedReader = inputStream.bufferedReader()
            var dataLine: String? = reader.readLine()


            //MARK: START of HTML section containing names of countries
            while (dataLine != null && !dataLine.contains("id=\"List_of_Asian_Countries\"")) {
                dataLine = reader.readLine()
            }


            //MARK: END of that HTML section
            while (dataLine != null && !dataLine.contains("id=\"Related_pages\"")){
                dataLine = reader.readLine()

                setupRegex(list, dataLine, "title=\"(.*?)\">")
            }

        } catch (e: Exception) {
            e.printStackTrace()

        } finally {
            if (inputStream != null) {
                inputStream.close()
            }
        }
    }


    fun downloadAmericanCountries(list: MutableList<String>) {
        val countriesURL = "https://en.wikipedia.org/wiki/List_of_countries_in_the_Americas_by_population"
        var inputStream: InputStream? = null

        try {
            inputStream = setupConnectionStream(countriesURL)

            val reader: BufferedReader = inputStream.bufferedReader()
            var dataLine: String? = reader.readLine()


            //MARK: START of HTML section containing names of countries
            while (dataLine != null && !dataLine.contains("<td>1</td>")) {
                dataLine = reader.readLine()
            }


            //MARK: END of that HTML section
            while (dataLine != null && !dataLine.contains("<td align=\"left\"><b>Total</b></td>")) {
                dataLine = reader.readLine()

                if (dataLine.contains("<td align=\"left\"><span class")) {
                    setupRegex(list, dataLine, "title=\"(.*?)\">")
                }
            }

            // Countries listed below do not have a flag
            // or it does not make sense to include them
            list.remove("Guadeloupe")
            list.remove("Martinique")
            list.remove("French Guiana")
            list.remove("Collectivity of Saint Martin")
            list.remove("Caribbean Netherlands")
            list.remove("Saint Barthélemy")
            list.remove("Saint Pierre and Miquelon")

            //MARK: Quickfix for American countries
            //region Quickfix - START
            //Removed by hand, ordered by Population @Wikipedia
            list.remove("United States")
            list.remove("France")
            list.remove("France")
            list.remove("Kingdom of the Netherlands")
            list.remove("Kingdom of the Netherlands")
            list.remove("United States")
            list.remove("United Kingdom")
            list.remove("United Kingdom")
            list.remove("Denmark")
            list.remove("Kingdom of the Netherlands")
            list.remove("United Kingdom")
            list.remove("France")
            list.remove("United Kingdom")
            list.remove("Kingdom of the Netherlands")
            list.remove("United Kingdom")
            list.remove("France")
            list.remove("France")
            list.remove("United Kingdom")
            list.remove("United Kingdom")
            //endregion Quickfix - END

            Log.d("Regex complete", list.toString())

        } catch (e: Exception) {
            e.printStackTrace()

        } finally {
            if (inputStream != null) {
                inputStream.close()
            }
        }
    }


    fun downloadAfricanCountries(list: MutableList<String>){
        var inputStream: InputStream? = null

        try {
            inputStream = setupConnectionStream(COUNTRIES_URL)

            val reader: BufferedReader = inputStream.bufferedReader()
            var dataLine: String? = reader.readLine()


            //MARK: START of HTML section containing names of countries
            while (dataLine != null && !dataLine.contains("<a href=\"/wiki/Africa\" title=\"Africa\">Africa</a>")) {
                dataLine = reader.readLine()
            }


            //MARK: END of that HTML section
            while (dataLine != null && !dataLine.contains("Zimbabwe</a></b></td>")){
                dataLine = reader.readLine()

                if (dataLine.contains("<span class")) {
                    setupRegex(list, dataLine, "title=\"(.*?)\">")
                }
            }

            // Countries listed below do not have a flag
            // or it does not make sense to include them
            list.remove("Réunion")
            list.remove("Mayotte")
            list.remove("Saint Helena, Ascension and Tristan da Cunha")

            Log.d("Regex complete", list.toString())

        } catch (e: Exception) {
            e.printStackTrace()

        } finally {
            if (inputStream != null) {
                inputStream.close()
            }
        }
    }


    fun downloadOceanicCountries(list: MutableList<String>){
        var inputStream: InputStream? = null

        try {
            inputStream = setupConnectionStream(COUNTRIES_URL)

            val reader: BufferedReader = inputStream.bufferedReader()
            var dataLine: String? = reader.readLine()


            //MARK: START of HTML section containing names of countries
            while (dataLine != null && !dataLine.contains("title=\"Australia (continent)\">Australia (continent)</a> and <a href=\"/wiki/Pacific_Islands\"")) {
                dataLine = reader.readLine()
            }


            //MARK: END of that HTML section
            while (dataLine != null && !dataLine.contains("Vanuatu</a></b></td>")){
                dataLine = reader.readLine()

                if (dataLine.contains("<span class=\"flagicon\"")) {
                    setupRegex(list, dataLine, "title=\"(.*?)\">")
                }
            }

            // Countries listed below do not have a flag
            // or it does not make sense to include them
            list.remove("Ashmore and Cartier Islands")
            list.remove("Baker Island")
            list.remove("Coral Sea Islands")
            list.remove("Howland Island")
            list.remove("Jarvis Island")
            list.remove("Johnston Atoll")
            list.remove("Kingman Reef")
            list.remove("Midway Atoll")
            list.remove("Palmyra Atoll")

            // Removed for now - their main pages have different og:images than flags
            list.remove("Easter Island")
            list.remove("New Caledonia")

            Log.d("Regex complete", list.toString())

        } catch (e: Exception) {
            e.printStackTrace()

        } finally {
            if (inputStream != null) {
                inputStream.close()
            }
        }
    }


    fun setupConnectionStream(countriesURL: String) : InputStream{
        val url = URL(countriesURL)
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.connect()

        return connection.inputStream
    }


    fun setupRegex(list: MutableList<String>, dataLine: String, regex: String){
        val p = Pattern.compile(regex)
        val m = p.matcher(dataLine)

        while (m.find()) {
            Log.d("Regex", m.group(1).toString())
            list.add(m.group(1))
        }
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