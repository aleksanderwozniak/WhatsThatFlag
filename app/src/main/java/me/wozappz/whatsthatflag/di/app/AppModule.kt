package me.wozappz.whatsthatflag.di.app

import dagger.Module
import dagger.Provides
import me.wozappz.whatsthatflag.app.App
import me.wozappz.whatsthatflag.data.Repository
import me.wozappz.whatsthatflag.data.model.DataLoader
import me.wozappz.whatsthatflag.data.model.Model
import me.wozappz.whatsthatflag.data.model.ModelImpl
import me.wozappz.whatsthatflag.data.prefs.SharedPrefsRepository
import me.wozappz.whatsthatflag.data.prefs.SharedPrefsRepositoryImpl
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
    fun provideModel(dataLoader: DataLoader): Model = ModelImpl(dataLoader)

    @Provides
    @Singleton
    fun provideSharedPrefsRepository(app: App): SharedPrefsRepository = SharedPrefsRepositoryImpl(app)

    @Provides
    @Singleton
    fun provideRepository(model: Model, sharedPrefsRepo: SharedPrefsRepository) = Repository(model, sharedPrefsRepo)
}