package com.olq.whatsthatflag.screens.start

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.olq.whatsthatflag.R
import com.olq.whatsthatflag.injector.Injector
import com.olq.whatsthatflag.screens.menu.MenuActivity
import org.jetbrains.anko.startActivity

class StartActivity : AppCompatActivity(), StartScreenContract.View {

    override lateinit var presenter: StartScreenContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        presenter = StartPresenter(this, Injector.provideModel())
        presenter.start()
    }


    override fun startMenuActivity() {
        startActivity<MenuActivity>()
    }
}
