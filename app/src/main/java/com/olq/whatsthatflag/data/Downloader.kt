package com.olq.whatsthatflag.data

import android.util.Log
import com.olq.whatsthatflag.screens.menu.MenuActivity
import java.io.BufferedReader
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

class Downloader {

    private val COUNTRIES_URL = "https://en.wikipedia.org/wiki/" +
            "List_of_sovereign_states_and_dependent_territories_by_continent"

    // each index stores data about size of particular continent summed with all previous continents
    val continentIds = IntArray(5)

    fun downloadAllFlags(): List<String> {
        var inputStream: InputStream? = null
        val listOfFlags = mutableListOf<String>()

        try {
            inputStream = setupConnectionStream(COUNTRIES_URL)

            val reader: BufferedReader = inputStream.bufferedReader()


            addAfrica(listOfFlags, reader)
            removeInvalidFlagsFromAfrica(listOfFlags)

            addAsia(listOfFlags, reader)
            removeInvalidFlagsFromAsia(listOfFlags)

            addEurope(listOfFlags, reader)
            removeInvalidFlagsFromEurope(listOfFlags)

            addNorthAmerica(listOfFlags, reader)
            addSouthAmerica(listOfFlags, reader)
            removeInvalidFlagsFromAmericas(listOfFlags)

            addOceania(listOfFlags, reader)
            removeInvalidFlagsFromOceania(listOfFlags)


        } catch (e: Exception) {
            e.printStackTrace()

        } finally {
            if (inputStream != null) {
                inputStream.close()
            }

            return listOfFlags
        }
    }


    private fun setupConnectionStream(countriesURL: String) : InputStream{
        val url = URL(countriesURL)
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.connect()

        return connection.inputStream
    }



    //region Continents to be added
    private fun addEurope(list: MutableList<String>, reader: BufferedReader){
        readSourceCode(list, reader,
                "Europe: political geography</a>.</i></p>",
                "Holy See</a></b></td>")
    }

    private fun addAsia(list: MutableList<String>, reader: BufferedReader){
        readSourceCode(list, reader,
                "Asia: territories and regions</a>.</i></p>",
                "Yemen</a></b></td>")
    }

    private fun addNorthAmerica(list: MutableList<String>, reader: BufferedReader){
        readSourceCode(list, reader,
                "North America: countries and territories</a>.</i></p>",
                "United States Virgin Islands</a></i></td>")
    }

    private fun addSouthAmerica(list: MutableList<String>, reader: BufferedReader){
        readSourceCode(list, reader,
                "South America: demographics</a>.</i></p>",
                "Venezuela</a></b></td>")
    }

    private fun addAfrica(list: MutableList<String>, reader: BufferedReader){
        readSourceCode(list, reader,
                "<a href=\"/wiki/Africa\" title=\"Africa\">Africa</a>",
                "Zimbabwe</a></b></td>")
    }

    private fun addOceania(list: MutableList<String>, reader: BufferedReader){
        readSourceCode(list, reader,
                "title=\"Australia (continent)\">Australia (continent)</a> and <a href=\"/wiki/Pacific_Islands\"",
                "Vanuatu</a></b></td>")
    }
    //endregion



    private fun readSourceCode(list: MutableList<String>, reader: BufferedReader,
                               readFrom: String, readTo: String) {
        var dataLine: String? = reader.readLine()

        //MARK: START of HTML section containing names of countries
        while (dataLine != null && !dataLine.contains(readFrom)) {
            dataLine = reader.readLine()
        }


        //MARK: END of that HTML section
        while (dataLine != null && !dataLine.contains(readTo)) {
            dataLine = reader.readLine()

            if (dataLine.contains("<span class=\"flagicon\"")) {
                setupRegex(list, dataLine, "title=\"(.*?)\">")
            }
        }
    }

    private fun setupRegex(list: MutableList<String>, dataLine: String, regex: String){
        val p = Pattern.compile(regex)
        val m = p.matcher(dataLine)

        while (m.find()) {
            Log.d("Regex", m.group(1).toString())
            list.add(m.group(1))
        }
    }



    //region Invalid flags to be removed
    private fun removeInvalidFlagsFromEurope(list: MutableList<String>) {
        list.remove("Svalbard")
        continentIds[2] = list.size
    }

    private fun removeInvalidFlagsFromAsia(list: MutableList<String>) {
        list.remove("Akrotiri and Dhekelia")
        list.remove("British Indian Ocean Territory")
        continentIds[1] = list.size
    }

    private fun removeInvalidFlagsFromAmericas(list: MutableList<String>) {
        list.remove("Bonaire")
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

        continentIds[3] = list.size
    }

    private fun removeInvalidFlagsFromAfrica(list: MutableList<String>) {
        list.remove("Réunion")
        list.remove("Mayotte")
        list.remove("Saint Helena, Ascension and Tristan da Cunha")
        continentIds[0] = list.size
    }

    private fun removeInvalidFlagsFromOceania(list: MutableList<String>) {
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

        continentIds[4] = list.size
    }
    //endregion



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

    private fun extractImgURL(inputStream: InputStream): String? {
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