package me.wozappz.whatsthatflag.di.app

import dagger.Component
import me.wozappz.whatsthatflag.di.game.GameScreenComponent
import me.wozappz.whatsthatflag.di.game.GameScreenModule
import me.wozappz.whatsthatflag.di.menu.MenuScreenComponent
import me.wozappz.whatsthatflag.di.menu.MenuScreenModule
import me.wozappz.whatsthatflag.di.start.StartScreenComponent
import me.wozappz.whatsthatflag.di.start.StartScreenModule
import javax.inject.Singleton

/**
 * Created by olq on 07.01.18.
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun plus(gameScreenModule: GameScreenModule): GameScreenComponent
    fun plus(menuScreenModule: MenuScreenModule): MenuScreenComponent
    fun plus(startScreenModule: StartScreenModule): StartScreenComponent
}