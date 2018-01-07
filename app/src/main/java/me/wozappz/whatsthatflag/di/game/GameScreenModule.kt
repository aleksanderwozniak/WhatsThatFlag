package me.wozappz.whatsthatflag.di.game

import dagger.Module
import dagger.Provides
import me.wozappz.whatsthatflag.data.Model
import me.wozappz.whatsthatflag.screens.game.GamePresenter
import me.wozappz.whatsthatflag.screens.game.GameScreenContract

/**
 * Created by olq on 08.01.18.
 */
@Module
class GameScreenModule(private val view: GameScreenContract.View) {

    @Provides
    fun provideGamePresenter(model: Model): GameScreenContract.Presenter = GamePresenter(view, model)
}