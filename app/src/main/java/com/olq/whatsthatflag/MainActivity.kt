package com.olq.whatsthatflag

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : Activity() {

    var id: Int = 0
    var score: Int = 0
    lateinit var downloader: Downloader

    var flagList = mutableListOf(
            "Poland",
            "Italy",
            "Germany",
            "England",
            "France",
            "Spain",
            "Zimbabwe"
    )


    val buttonNames = arrayOf<String>(
            "", "", "", ""
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        randomizeFlagList()

        downloader = Downloader(myImgView, myProgressBar)
        downloader.downloadContent(getURLFromName(flagList.get(id)))

        renameBtns()
    }


    fun randomizeFlagList() {
        Collections.shuffle(flagList)
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

        downloader.downloadContent(
                getURLFromName(flagList.get(id)))
    }

    fun incrementScore(){
        score++
        myTextView.text = "Score: $score"
    }

    fun getURLFromName(countryName: String): String{
        return "https://en.wikipedia.org/wiki/$countryName"
    }
}
