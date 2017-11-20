package com.olq.whatsthatflag.screens.game

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.olq.whatsthatflag.model.Downloader
import com.olq.whatsthatflag.R
import com.olq.whatsthatflag.screens.menu.MenuActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_game.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class GameActivity : AppCompatActivity() {

    var id: Int = 0
    var score: Int = 0

    var flagList = mutableListOf<String>()
    val buttonNames = arrayOf<String>("", "", "", "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val amountOfCountries = intent.getIntExtra("AMOUNT_OF_COUNTRIES", 20)
        val selectedContinent = intent.getSerializableExtra("SELECTED_CONTINENT") as MenuActivity.CONTINENT

        val categoryText = selectedContinent.toString().toLowerCase().capitalize()

        categoryTextView.text = "Category: $categoryText"

        showProgressBar()

        doAsync {
            Downloader.downloadContinent(selectedContinent, flagList)

            uiThread {
                hideProgressBar()

                Collections.shuffle(flagList)

                if(amountOfCountries != -1) {
                    splitFlagList(flagList, amountOfCountries)
                }

                loadImg()
                renameBtns()
            }
        }
    }

    fun splitFlagList(flagList: MutableList<String>, amountOfCountries: Int){
        flagList.split(amountOfCountries)

        Log.d("splitFlagList", flagList.size.toString() + "\n" + flagList.toString())
    }

    fun MutableList<String>.split(fromIndex: Int){
        for (i in fromIndex..this.size-1){
            this.removeAt(0)
        }
    }

    fun loadImg(){
        showProgressBar()

        doAsync {
        val currentURL: String ?= Downloader.getImgURL(getURLFromName(flagList[id]))

            uiThread {
                if(currentURL != null) {
                    Log.d("Download Image", "CurrentURL: $currentURL")
                    myImgView.loadUrl(currentURL)
                }

                Log.d("Download Image", "Finished")
                hideProgressBar()
            }
        }
    }

    fun ImageView.loadUrl(url: String) {
        Picasso.with(context).load(url).into(this)
    }

    fun renameBtns(){
        randomizeButtonNames()

        myBtnA.text = buttonNames[0]
        myBtnB.text = buttonNames[1]
        myBtnC.text = buttonNames[2]
        myBtnD.text = buttonNames[3]
    }

    fun randomizeButtonNames(){
        clearBtnNames()

        buttonNames[0] = flagList[id]
        buttonNames[1] = flagList[randomNoDuplicate(id, flagList.size-1)]
        buttonNames[2] = flagList[randomNoDuplicate(id, flagList.size-1)]
        buttonNames[3] = flagList[randomNoDuplicate(id, flagList.size-1)]

        buttonNames.sort()
    }

    fun clearBtnNames(){
        for (i in 0..buttonNames.size - 1){
            buttonNames[i] = ""
        }
    }

    fun randomNoDuplicate(from: Int, to: Int): Int {
        var random = Random().rand(from, to)

        while(isInButtonNames(random)) {
            random = Random().rand(0, to)
        }

        return random
    }

    fun Random.rand(from: Int, to: Int): Int{
        if(from == to) return nextInt(to)
        else return nextInt(to - from) + from
    }

    fun isInButtonNames(index: Int): Boolean{
        return (buttonNames.contains(flagList[index]))
    }



    fun btnClicked(view: View){
        when((view as Button).text){
            flagList[id] -> {
                reloadFlag()
                incrementScore()
            }

            else -> {
                reloadFlag()
            }
        }
    }

    fun reloadFlag(){
        id++
        if(id == flagList.size) id = 0

        loadImg()
        renameBtns()
    }

    fun incrementScore(){
        score++
        scoreTextView.text = "Score: $score"
    }

    fun getURLFromName(countryName: String): String{
        return "https://en.wikipedia.org/wiki/$countryName"
    }


    fun showProgressBar(){
        myProgressBar.visibility = View.VISIBLE
        myImgView.visibility = View.INVISIBLE
    }

    fun hideProgressBar(){
        myProgressBar.visibility = View.INVISIBLE
        myImgView.visibility = View.VISIBLE
    }
}
