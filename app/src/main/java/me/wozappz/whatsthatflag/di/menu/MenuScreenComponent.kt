package me.wozappz.whatsthatflag.di.menu

import dagger.Subcomponent
import me.wozappz.whatsthatflag.screens.menu.MenuActivity

/**
 * Created by olq on 07.01.18.
 */
@Subcomponent(modules = [MenuScreenModule::class])
interface MenuScreenComponent {

    fun injectTo (activity: MenuActivity)
}