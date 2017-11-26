package com.olq.whatsthatflag.screens.start

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.olq.whatsthatflag.R
import com.olq.whatsthatflag.injector.Injector
import com.olq.whatsthatflag.screens.menu.MenuActivity
import com.olq.whatsthatflag.utils.checkInternetConnection
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
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

    override fun isConnectedToInternet(): Boolean {
        return checkInternetConnection(applicationContext)
    }

    override fun showNoConnectionAlert() {
        val internetErrorAlert = alert (Appcompat) {
            title = "Connection error!"
            message = "Make sure you are connected to Internet, then restart the App"

            negativeButton("Exit", { finishAffinity() })
        }.build()

        internetErrorAlert.setCancelable(false)
        internetErrorAlert.setCanceledOnTouchOutside(false)
        internetErrorAlert.show()
    }
}
