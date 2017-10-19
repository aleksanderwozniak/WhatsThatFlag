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
        var inputStream: InputStream? = null

        try {
            inputStream = setupConnectionStream(COUNTRIES_URL)

            val reader: BufferedReader = inputStream.bufferedReader()
            var dataLine: String? = reader.readLine()


            //MARK: START of HTML section containing names of countries
            while (dataLine != null && !dataLine.contains("Europe: political geography</a>.</i></p>")) {
                dataLine = reader.readLine()
            }


            //MARK: END of that HTML section
            while (dataLine != null && !dataLine.contains("Holy See</a></b></td>")){
                dataLine = reader.readLine()

                if (dataLine.contains("<span class=\"flagicon\"")) {
                    setupRegex(list, dataLine, "title=\"(.*?)\">")
                }
            }

            // Countries listed below do not have a flag
            // or it does not make sense to include them
            list.remove("Svalbard")

        } catch (e: Exception) {
            e.printStackTrace()

        } finally {
            if (inputStream != null) {
                inputStream.close()
            }
        }
    }


    fun downloadAsianCountries(list: MutableList<String>){
        var inputStream: InputStream? = null

        try {
            inputStream = setupConnectionStream(COUNTRIES_URL)

            val reader: BufferedReader = inputStream.bufferedReader()
            var dataLine: String? = reader.readLine()


            //MARK: START of HTML section containing names of countries
            while (dataLine != null && !dataLine.contains("Asia: territories and regions</a>.</i></p>")) {
                dataLine = reader.readLine()
            }


            //MARK: END of that HTML section
            while (dataLine != null && !dataLine.contains("Yemen</a></b></td>")){
                dataLine = reader.readLine()

                if (dataLine.contains("<span class=\"flagicon\"")) {
                    setupRegex(list, dataLine, "title=\"(.*?)\">")
                }
            }

            // Countries listed below do not have a flag
            // or it does not make sense to include them
            list.remove("Akrotiri and Dhekelia")
            list.remove("British Indian Ocean Territory")


        } catch (e: Exception) {
            e.printStackTrace()

        } finally {
            if (inputStream != null) {
                inputStream.close()
            }
        }
    }


    fun downloadAmericanCountries(list: MutableList<String>) {
        var inputStream: InputStream? = null

        try {
            inputStream = setupConnectionStream(COUNTRIES_URL)

            val reader: BufferedReader = inputStream.bufferedReader()

            downloadNorthAmerica(list, reader)
            downloadSouthAmerica(list, reader)

            // Countries listed below do not have a flag
            // or it does not make sense to include them
            list.remove("Clipperton Island")
            list.remove("Guadeloupe")
            list.remove("Martinique")
            list.remove("Saint Barthélemy")
            list.remove("Collectivity of Saint Martin")
            list.remove("Saint Pierre and Miquelon")
            list.remove("French Guiana")

            // Those countries could be included,
            // but their og:images are not flags
            list.remove("Saba")
            list.remove("Sint Eustatius")
            list.remove("Navassa Island")

            Log.d("Regex complete", list.toString())

        } catch (e: Exception) {
            e.printStackTrace()

        } finally {
            if (inputStream != null) {
                inputStream.close()
            }
        }
    }

    private fun downloadNorthAmerica(list: MutableList<String>, reader: BufferedReader){
        var dataLine: String? = reader.readLine()

        //MARK: START of HTML section containing names of countries
        while (dataLine != null && !dataLine.contains("North America: countries and territories</a>.</i></p>")) {
            dataLine = reader.readLine()
        }


        //MARK: END of that HTML section
        while (dataLine != null && !dataLine.contains("United States Virgin Islands</a></i></td>")) {
            dataLine = reader.readLine()

            if (dataLine.contains("<span class=\"flagicon\"")) {
                setupRegex(list, dataLine, "title=\"(.*?)\">")
            }
        }
    }

    private fun downloadSouthAmerica(list: MutableList<String>, reader: BufferedReader){
        var dataLine: String? = reader.readLine()

        //MARK: START of HTML section containing names of countries
        while (dataLine != null && !dataLine.contains("South America: demographics</a>.</i></p>")) {
            dataLine = reader.readLine()
        }


        //MARK: END of that HTML section
        while (dataLine != null && !dataLine.contains("Venezuela</a></b></td>")) {
            dataLine = reader.readLine()

            if (dataLine.contains("<span class=\"flagicon\"")) {
                setupRegex(list, dataLine, "title=\"(.*?)\">")
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