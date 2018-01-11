package me.wozappz.whatsthatflag.di.start

import dagger.Module
import dagger.Provides
import me.wozappz.whatsthatflag.data.Repository
import me.wozappz.whatsthatflag.screens.start.StartPresenter
import me.wozappz.whatsthatflag.screens.start.StartScreenContract

/**
 * Created by olq on 07.01.18.
 */
@Module
class StartScreenModule(private val view: StartScreenContract.View) {

    @Provides
    fun provideStartPresenter(repo: Repository): StartScreenContract.Presenter = StartPresenter(view, repo)
}