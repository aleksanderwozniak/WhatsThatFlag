package com.olq.whatsthatflag

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.util.*


class MainActivity : AppCompatActivity() {

    var id: Int = 0
    var score: Int = 0

    val AMOUNT_OF_COUNTRIES = 20

    lateinit var downloader: Downloader

    var flagList = mutableListOf<String>()


    val buttonNames = arrayOf<String>(
            "", "", "", ""
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        downloader = Downloader(myImgView, myProgressBar)
        downloader.downloadListOfCountries(flagList, AMOUNT_OF_COUNTRIES)

        //MARK: Issue
        // Countries are loaded in async, but views are not updated after being ready to be used.
        // This results in first btnClick being empty, and only after it the App will work as intended.

        toast("Press any button") // This is not rly a solution... But it works for now.
    }


    fun loadImg(){
        downloader.downloadImage(
                getURLFromName(flagList[id]))
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

        renameBtns()
        loadImg()
    }

    fun incrementScore(){
        score++
        myTextView.text = "Score: $score"
    }

    fun getURLFromName(countryName: String): String{
        return "https://en.wikipedia.org/wiki/$countryName"
    }
}
