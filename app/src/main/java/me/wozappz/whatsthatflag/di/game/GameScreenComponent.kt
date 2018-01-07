package me.wozappz.whatsthatflag.di.game

import dagger.Subcomponent
import me.wozappz.whatsthatflag.screens.game.GameActivity

/**
 * Created by olq on 08.01.18.
 */
@Subcomponent(modules = [GameScreenModule::class])
interface GameScreenComponent {

    fun injectTo (activity: GameActivity)
}