package me.wozappz.whatsthatflag.screens.start

import android.graphics.Color
import android.graphics.drawable.Animatable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_start.*
import me.wozappz.whatsthatflag.R
import me.wozappz.whatsthatflag.utils.checkInternetConnection
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity

class StartActivity : AppCompatActivity(), me.wozappz.whatsthatflag.screens.start.StartScreenContract.View {

    override lateinit var presenter: me.wozappz.whatsthatflag.screens.start.StartScreenContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        window.decorView.setBackgroundColor(Color.BLACK)

        presenter = me.wozappz.whatsthatflag.screens.start.StartPresenter(this, me.wozappz.whatsthatflag.injector.Injector.provideModel(applicationContext))
        presenter.start()

        (mTrgAnim.drawable as Animatable).start()
    }


    override fun startMenuActivity() {
        startActivity<me.wozappz.whatsthatflag.screens.menu.MenuActivity>()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun isConnectedToInternet(): Boolean {
        return checkInternetConnection(applicationContext)
    }

    override fun showNoConnectionAlert() {
        val internetErrorAlert = alert (Appcompat) {
            title = getString(R.string.alert_start_internet_error_title)
            message = getString(R.string.alert_start_internet_error_msg)

            negativeButton(getString(R.string.alert_start_internet_error_btn_neg), { finishAffinity() })
        }.build()

        internetErrorAlert.setCancelable(false)
        internetErrorAlert.setCanceledOnTouchOutside(false)
        internetErrorAlert.show()

        val typeface = ResourcesCompat.getFont(this, R.font.lato)

        val netErrorTitle = internetErrorAlert.find<TextView>(android.support.v7.appcompat.R.id.alertTitle)
        netErrorTitle.typeface = typeface

        val netErrorMessage = internetErrorAlert.find<TextView>(android.R.id.message)
        netErrorMessage.typeface = typeface

        val netErrorExitBtn = internetErrorAlert.getButton(AlertDialog.BUTTON_NEGATIVE)
        netErrorExitBtn.typeface = typeface
    }
}
