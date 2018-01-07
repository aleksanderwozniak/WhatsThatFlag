package me.wozappz.whatsthatflag.di.menu

import dagger.Module
import dagger.Provides
import me.wozappz.whatsthatflag.data.Model
import me.wozappz.whatsthatflag.screens.menu.MenuPresenter
import me.wozappz.whatsthatflag.screens.menu.MenuScreenContract

/**
 * Created by olq on 07.01.18.
 */
@Module
class MenuScreenModule(private val view: MenuScreenContract.View) {

    @Provides
    fun provideMenuPresenter(model: Model): MenuScreenContract.Presenter = MenuPresenter(view, model)
}