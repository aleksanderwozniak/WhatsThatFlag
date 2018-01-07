package me.wozappz.whatsthatflag.di.app

import dagger.Module
import dagger.Provides
import me.wozappz.whatsthatflag.app.App
import me.wozappz.whatsthatflag.data.DataLoader
import me.wozappz.whatsthatflag.data.Model
import javax.inject.Singleton

/**
 * Created by olq on 07.01.18.
 */
@Module
class AppModule(private val app: App) {

    @Provides
    @Singleton
    fun provideApp(): App = app

    @Provides
    @Singleton
    fun provideDataLoader(app: App) = DataLoader(app)

    @Provides
    @Singleton
    fun provideModel(dataLoader: DataLoader) = Model(dataLoader)
}