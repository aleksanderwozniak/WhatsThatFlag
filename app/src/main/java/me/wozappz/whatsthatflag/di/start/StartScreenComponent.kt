package me.wozappz.whatsthatflag.di.start

import dagger.Subcomponent
import me.wozappz.whatsthatflag.screens.start.StartActivity

/**
 * Created by olq on 07.01.18.
 */
@Subcomponent(modules = [StartScreenModule::class])
interface StartScreenComponent {

    fun injectTo (activity: StartActivity)
}