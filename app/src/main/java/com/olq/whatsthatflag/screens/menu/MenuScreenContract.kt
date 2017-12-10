package com.olq.whatsthatflag.screens.menu

import com.olq.whatsthatflag.screens.BaseView

/**
 * Created by olq on 10.12.17.
 */
interface MenuScreenContract {

    interface View: BaseView<Presenter> {
        fun startGameActivityWithDelay(duration: Long)
    }

    interface Presenter {
        fun start()
        fun restartWtfDividerAnimation()
        fun startGlobeAnimation()
        fun hideWtfDivider(duration: Long)
        fun btnStartClicked()
    }
}