package me.wozappz.whatsthatflag.data

import me.wozappz.whatsthatflag.data.model.Model
import me.wozappz.whatsthatflag.data.prefs.SharedPrefsRepository
import javax.inject.Inject

/**
 * Created by olq on 11.01.18.
 */
class Repository @Inject constructor(
        val model: Model,
        val prefsRepo: SharedPrefsRepository) {
}