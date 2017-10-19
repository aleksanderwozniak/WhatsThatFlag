package com.olq.whatsthatflag

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TableLayout
import android.widget.TableRow

/**
 * Created by olq on 17.10.17.
 */

class RadioGroupTableLayout: TableLayout, View.OnClickListener {

    private var activeRadioButton: RadioButton? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    override fun onClick(view: View) {
        val tempBtn = view as RadioButton

        if (activeRadioButton != null) {
            activeRadioButton!!.isChecked = false
        }

        tempBtn.isChecked = true
        activeRadioButton = tempBtn
    }


    override fun addView(child: View?) {
        super.addView(child)
        setChildrenOnClickListener(child as TableRow)
    }

    override fun addView(child: View?, index: Int) {
        super.addView(child, index)
        setChildrenOnClickListener(child as TableRow)
    }

    override fun addView(child: View?, width: Int, height: Int) {
        super.addView(child, width, height)
        setChildrenOnClickListener(child as TableRow)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        setChildrenOnClickListener(child as TableRow)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        setChildrenOnClickListener(child as TableRow)
    }

    private fun setChildrenOnClickListener(tableRow: TableRow) {
        val amount = tableRow.childCount
        for (i in 0..amount - 1) {
            val child = tableRow.getChildAt(i)
            (child as? RadioButton)?.setOnClickListener(this)
        }
    }


    fun getCheckedRadioButtonId(): Int {
        if (activeRadioButton != null) {
            return activeRadioButton!!.id
        }

        return -1
    }
}