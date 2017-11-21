package com.olq.whatsthatflag.screens.game

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.olq.whatsthatflag.R
import com.olq.whatsthatflag.injector.Injector
import com.olq.whatsthatflag.screens.menu.MenuActivity
import com.olq.whatsthatflag.utils.loadUrl
import kotlinx.android.synthetic.main.activity_game.*


class GameActivity : AppCompatActivity(), GameScreenContract.View {

    override lateinit var presenter: GameScreenContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val amountOfCountries = intent.getIntExtra("AMOUNT_OF_COUNTRIES", 20)
        val selectedContinent = intent.getSerializableExtra("SELECTED_CONTINENT") as MenuActivity.CONTINENT

        val categoryText = selectedContinent.toString().toLowerCase().capitalize()

        categoryTextView.text = getString(R.string.category_text, categoryText)

        presenter = GamePresenter(this, Injector.provideModel())
        presenter.start(Pair(selectedContinent, amountOfCountries))
    }


    fun btnClicked(view: View) {
        val countryName = (view as Button).text.toString()

        presenter.answerBtnClicked(countryName)
    }


    override fun loadImg(currentUrl: String){
        myImgView.loadUrl(currentUrl)
    }

    override fun renameButtons(btnNames: List<String>) {
        myBtnA.text = btnNames[0]
        myBtnB.text = btnNames[1]
        myBtnC.text = btnNames[2]
        myBtnD.text = btnNames[3]
    }

    override fun showScore(score: Int) {
        scoreTextView.text = getString(R.string.score_text, score)
    }

    override fun showProgressBar() {
        myProgressBar.visibility = View.VISIBLE
        myImgView.visibility = View.INVISIBLE
    }

    override fun hideProgressBar() {
        myProgressBar.visibility = View.INVISIBLE
        myImgView.visibility = View.VISIBLE
    }
}
