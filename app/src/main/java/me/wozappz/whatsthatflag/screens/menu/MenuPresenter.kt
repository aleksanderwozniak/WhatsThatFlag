package me.wozappz.whatsthatflag.screens.menu

import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import me.wozappz.whatsthatflag.data.Model
import org.jetbrains.anko.coroutines.experimental.bg
import javax.inject.Inject


/**
 * Created by olq on 10.12.17.
 */
class MenuPresenter @Inject constructor(
        private val view: MenuScreenContract.View,
        private val model: Model)
    : MenuScreenContract.Presenter {


    override fun start() {
        view.setupGlobeAnimation()
        view.animateViewsAlpha(0.2f, 0)
    }

    override fun restartWtfDividerAnimation() {
        view.hideWtfDivider()
        view.showWtfDivider(800, 200)
    }

    override fun startGlobeAnimation() {
        view.animateViewsAlpha(1f, 1200)
        view.runCompoundGlobeAnimation()
        view.animateBackgroundColor()
    }


    override fun btnInfoClicked() {
        view.showAppInfo()
    }

    override fun btnSourceClicked() {
        view.showGitHubSourceInBrowser()
    }

    override fun btnStartClicked() {
        val amount = calculateAmountOfCountries(view.getFlagSeekbarProgress())
        val continent = view.getSelectedContinent()

        view.setStartButtonClickability(false)

        async(UI) {
            bg {
//                 setup list of flags for next quiz
                model.selectFlags(Pair(continent, amount))
            }.await()


            if (continent == CONTINENT.OCEANIA && amount == 40) {
                val flagCount = model.flagList.size
                view.displayMessageOceaniaMaxFlags(flagCount)
            }

            // starting a new activity has some lag to it, thus durations below are not equal
            view.hideWtfDivider(600)
            view.startGameActivityWithDelay(continent, 400)
        }
    }


    private fun calculateAmountOfCountries(seekbarProgress: Int): Int {
        when (seekbarProgress) {
            0 -> return 5
            1 -> return 10
            2 -> return 20
            3 -> return 40
            4 -> return -1 // ALL = -1

            else -> { return -1 }
        }
    }

    override fun flagSeekbarProgressChanged(progress: Int) {
        val amount = calculateAmountOfCountries(progress)

        if (amount == -1) {
            view.showFlagSeekbarLabelAll()
        } else {
            view.showFlagSeekbarLabel(amount)
        }
    }
}